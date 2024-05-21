package pl.kurs;


import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.adapter.ThrowsAdviceInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.kurs.model.Book;
import pl.kurs.service.BookIdGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws InterruptedException {

//
//        SpringApplication.run(Main.class, args);

//
//        List<Integer> numbers = Collections.synchronizedList(new ArrayList<>());
//
//        MyThread mt1 = new MyThread(numbers, 1);
//        MyThread mt2 = new MyThread(numbers, 2);
//        MyThread mt3 = new MyThread(numbers, 3);
//        MyThread mt4 = new MyThread(numbers, 4);
//
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
//    }
//
//
//    @RequiredArgsConstructor
//    static class MyThread extends Thread {
//        private final List<Integer> numbers;
//        private final int number;
//
//        @Override
//        public void run() {
//            for (int i = 0; i < 1000; i++) {
//                numbers.add(number);
//            }
//        }

        // Test: wykorzystaj 10 watkow i kazdy watek dodaje 1000 roznych ksiazek
        // wypisz ile jest wszystkich ksiazek: ArrayList/SynchronizedList

        // dopisanie testow do pozostalych metod
        // i uzupelnienie testow w klasie carController i garageController

        List<Book> books = Collections.synchronizedList(new ArrayList<>());
//        List<Book> books = new ArrayList<>();
        MyThread[] threads = new MyThread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new MyThread(books);
            threads[i].start();
        }
        for (MyThread thread : threads) {
            thread.join();
        }

        System.out.println("Number of books: " + books.size());


    }

    @RequiredArgsConstructor
    static class MyThread extends Thread {
        private final List<Book> books;
        private BookIdGenerator bookIdGenerator = new BookIdGenerator();
        private Random random = new Random();
        private List<String> titleList = List.of("title1", "title2", "title3", "title4", "title5", "title6", "title7", "title8");
        private List<String> categoryList = List.of("category1", "category2", "category3", "category4", "category5", "category6", "category7", "category8");

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                Book book = new Book(bookIdGenerator.getId(), titleList.get(random.nextInt(8)), categoryList.get(random.nextInt(8)), true);
                books.add(book);
            }
        }
    }
}

