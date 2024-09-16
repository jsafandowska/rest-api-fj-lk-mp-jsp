package pl.kurs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class  Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        // praca domowa obejrzec zajecia paginacja z fetchowaniem, przygotowac sobie pytania jesli takie sie pojawią

        // dodac podobny mechanizm w authorze podczas edycji ma byc sprawdzana wersja
    }
}

