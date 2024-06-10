package pl.kurs;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        // poprawa testow, jesli ktores sie tertaz wywalają
        // dodac dwa testy na dodanie i usunięcie samochodu
        // postarać się stworzyć warstewe serwisową
        // na razie mamy kontroler i repozytorium a powinnismy miec warstwe controlera ktora przyjmuje zadanie http i serwis ktory wykonuje logike
        // serwis przyjmuje repozytorium a kontroler przyjmuje serwis


    }

}

