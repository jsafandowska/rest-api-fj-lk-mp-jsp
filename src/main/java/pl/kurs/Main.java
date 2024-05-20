package pl.kurs;


import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.adapter.ThrowsAdviceInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(Main.class, args);
        List<Book> books = Collections.synchronizedList(new ArrayList<>());

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new BookThread(books));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Total number of books: " + books.size());
    }

    @RequiredArgsConstructor
    public static class Book {
        private final String title;
        private final String author;
    }

    @RequiredArgsConstructor
    public static class BookThread implements Runnable {
        private final List<Book> books;
        @Override
        public void run() {
            Random random = new Random();
            String[] titles = {"Title1", "Title2", "Title3", "Title4", "Title5"};
            String[] authors = {"Author1", "Author2", "Author3", "Author4", "Author5"};
            for (int i = 0; i < 1000; i++) {
                Book book = new Book(titles[random.nextInt(titles.length)], authors[random.nextInt(authors.length)]);
                synchronized (books) {
                    books.add(book);
                }
            }
        }
    }
}


//        List<Integer> numbers = Collections.synchronizedList(new ArrayList<>());
//
//        MyThread mt1 = new MyThread(numbers, 1);
//        MyThread mt2 = new MyThread(numbers, 2);
//        MyThread mt3 = new MyThread(numbers, 3);
//        MyThread mt4 = new MyThread(numbers, 4);
//
//
//        mt1.start();
//        mt2.start();
//        mt3.start();
//        mt4.start();
//
//        mt1.join();
//        mt2.join();
//        mt3.join();
//        mt4.join();
//
//        System.out.println("ilosc elementow: " + numbers.size());
//        System.out.println("ilosc 1: " + numbers.stream().filter(i -> i == 1).count());
//        System.out.println("ilosc 2: " + numbers.stream().filter(i -> i == 2).count());
//        System.out.println("ilosc 3: " + numbers.stream().filter(i -> i == 3).count());
//        System.out.println("ilosc 4: " + numbers.stream().filter(i -> i == 4).count());
//

// Test: wykorzystaj 10 watkow i kazdy watek dodaje 1000 roznych ksiazek
// wypisz ile jest wszystkich ksiazek: ArrayList/SynchronizedList

// dopisanie testow do pozostalych metod
// i uzupelnienie testow w klasie carController i garageController

