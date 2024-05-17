package pl.kurs;


import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.adapter.ThrowsAdviceInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);


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
    }
}