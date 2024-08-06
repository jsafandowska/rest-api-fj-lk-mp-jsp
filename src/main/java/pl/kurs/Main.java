package pl.kurs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        // PD - stworz jeden endpoint w ktorym mozemy dodawac dowolną osobę
        // powinna byc taka konstrukcja, taki kontrakt, zeby dodanie nowego typu osoby nie sprawialo koniecznosci zmian w istniejacych juz klasach
        // wykorzystac fasady
    }

}

