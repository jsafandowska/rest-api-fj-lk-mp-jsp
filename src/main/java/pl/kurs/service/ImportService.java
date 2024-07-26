package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.faade.ImportStatusFacade;
import pl.kurs.model.ImportStatus;
import pl.kurs.repository.ImportStatusRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {

    private static final String INSERT_BOOK_SQL = "insert into book(title, category, available, author_id) values (?,?,?,?)";
    private final JdbcTemplate jdbcTemplate;
    private final ImportStatusFacade importStatusFacade;

    @Transactional
    private void saveBook(String[] args) {
        jdbcTemplate.update(INSERT_BOOK_SQL,
                args[0], args[1], true, Integer.parseInt(args[2]));
    }

    public ImportStatus startImport(String fileName) {
        return importStatusFacade.saveAndFlush(new ImportStatus(fileName));
    }

    @Transactional
    @Async("booksImportExecutor")
    public void importBooks(InputStream fileReader, int id) {
        importStatusFacade.updateToProcessing(id);
        AtomicInteger counter = new AtomicInteger(0);
        AtomicLong start = new AtomicLong(System.currentTimeMillis());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileReader))) {
            reader.lines()
                    .map(line -> line.split(","))
                    .peek(command -> countTime(counter, start, id))
                    .forEach(this::saveBook);
            importStatusFacade.updateToSuccess(id, counter.get());
        } catch (IOException e) {
            log.error("Error during import", e);
            importStatusFacade.updateToFail(id, e.getMessage(), counter.get());
            throw new IllegalStateException("wycofanie transakcji");
        }

    }


    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public ImportStatus findById(int id) {
        return importStatusFacade.findById(id).orElseThrow();
    }

    private void countTime(AtomicInteger counter, AtomicLong start, int id) {
        int progress = counter.incrementAndGet();
        if (progress % 10000 == 0) {
            log.info("Imported: {} in {} ms", counter, (System.currentTimeMillis() - start.get()));
            start.set(System.currentTimeMillis());
            //update statusu
            importStatusFacade.updateProgress(id, progress);
        }
    }



}
