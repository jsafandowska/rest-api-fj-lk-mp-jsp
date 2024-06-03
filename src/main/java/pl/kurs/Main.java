package pl.kurs;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);


        // poprawić testy w BookController, po dodaniu autora

        // sprobowac zrobic połączenie jeden do wiele pomiedzy Car i Garage
        // w pozostałych kontrolerach zmienic w metodach http encje na dto encji
        // nalezy pamietac, ze samochodu nie mozna dodac jesli garaz jest pelny,
        // nie mozna dodac samochodu na lpg jesli garaz nie dopuszcza takich pojazdow
    }

}

