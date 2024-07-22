package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.model.ImportStatus;
import pl.kurs.repository.ImportStatusRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {

    private static final String INSERT_BOOK_SQL = "insert into book(title, category, available, author_id) values (?,?,?,?)";
    private final JdbcTemplate jdbcTemplate;
    private final ImportStatusRepository importStatusRepository;

    @Transactional
    private void saveBook(String[] args) {
        jdbcTemplate.update(INSERT_BOOK_SQL,
                args[0], args[1], true, Integer.parseInt(args[2]));
    }

    public ImportStatus startImport(String fileName){
        return importStatusRepository.saveAndFlush(new ImportStatus(fileName));
    }

    @Transactional
    @Async("booksImportExecutor")
    public void importBooks(InputStream fileReader, int id) {

        // rozpocxzaecie procesowania
        AtomicInteger counter = new AtomicInteger(0);
        AtomicLong start = new AtomicLong(System.currentTimeMillis());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileReader))) {
            reader.lines()
                    .map(line -> line.split(","))
                    .peek(command -> countTime(counter, start))
                    .forEach(this::saveBook);
        } catch (IOException e) {
            log.error("Error reading file", e);
//            koniec z bledem
        }

        // koniec z sukcesem
    }

    private void countTime(AtomicInteger counter, AtomicLong start) {
        if (counter.incrementAndGet() % 10000 == 0) {
            log.info("Imported: {} in {} ms", counter, (System.currentTimeMillis() - start.get()));
            start.set(System.currentTimeMillis());
            //update statusu
        }
    }
}
