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
import java.time.LocalDateTime;
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
    public void saveBook(String[] args) {
        jdbcTemplate.update(INSERT_BOOK_SQL,
                args[0], args[1], true, Integer.parseInt(args[2]));
    }

    public ImportStatus startImport(String fileName){
        return importStatusRepository.saveAndFlush(new ImportStatus(fileName));
    }

    @Transactional
    @Async("booksImportExecutor")
    public void importBooks(InputStream fileReader, int id) {

        AtomicInteger counter = new AtomicInteger(0);
        AtomicLong start = new AtomicLong(System.currentTimeMillis());
        ImportStatus importStatus = importStatusRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid import id: " + id));
        importStatus.setStatus(ImportStatus.Status.PROCESSING);
        importStatus.setStartDate(LocalDateTime.now());
        importStatusRepository.save(importStatus);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileReader))) {
            reader.lines()
                    .map(line -> line.split(","))
                    .peek(command -> countTime(counter, start, importStatus))
                    .forEach(this::saveBook);
            importStatus.setStatus(ImportStatus.Status.SUCCESS);
            importStatus.setFinishDate(LocalDateTime.now());
            importStatus.setPreocessed(counter.get());
            importStatusRepository.save(importStatus);
        } catch (IOException e) {
            log.error("There is an error", e);
            importStatus.setStatus(ImportStatus.Status.FAILED);
            importStatus.setFinishDate(LocalDateTime.now());
            importStatus.setFailedReason(e.getMessage());
            importStatusRepository.save(importStatus);
        }
    }

    private void countTime(AtomicInteger counter, AtomicLong start, ImportStatus importStatus) {
        if (counter.incrementAndGet() % 10000 == 0) {
            log.info("Imported: {} in {} ms", counter, (System.currentTimeMillis() - start.get()));
            start.set(System.currentTimeMillis());
            importStatus.setPreocessed(counter.get());
            importStatusRepository.save(importStatus);
        }
    }
}