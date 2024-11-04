package pl.kurs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class  Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        // dodac nowe adnotacje do uzupelnienia swaggera,
        // co znajdziecie im wiecej tym lepiej, nie wszedzie zeby nie zasmiecac,
        // w garage controller po prostu


        // dodac endpoint do pobierania wszystkich samochodow z konkretnego garazu
        // i np podczas zwracania garzu po id, moglibysmy wyswietlac rowzniez link,
        // ktory przekierowuje do metody pobierajacej samochody z danego garazu

        // pozmieniac wyjatki zeby nie lecialy 500

        // kolejne zajęcia - walidacje

        // https://spring.io/guides/gs/rest-hateoas
    }
}

