package pl.kurs.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BookIdGenerator {

    private final AtomicInteger idGenerator = new AtomicInteger(0);

    public int getId(){
        return idGenerator.incrementAndGet();
    }
}
