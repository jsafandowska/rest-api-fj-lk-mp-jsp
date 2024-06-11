package pl.kurs;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
        // poprawa testow, jesli ktores sie teraz wywalaja
        // dodac dwa testy na dodanie i usuniecie samchodu
        // postarac sie stworzyc warstwe seriwsowa
        // na razie mamy  kontroler i repozytorium a powinismy miec warstwe controlera ktora przyjmuje zapytanie http
        // i seriws ktory wykonuje logike
        // serwis przyjmuje repozytorium a kontroler przyjmuje seriws
    }

}

