package pl.kurs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class  Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        // aktualizacja liquibase po dodaniu slownikow
        // sprawdzic testy
        // uzupelnic w data loader Persony, zeby kazdy mial jakies country i employee zeby mial position
    }
}

