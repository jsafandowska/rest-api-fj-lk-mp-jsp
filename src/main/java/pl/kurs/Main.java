package pl.kurs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        //todo poprawic testy w controlerach zeby uzywaly serwisow, uzupelnic authorController i autorService
        // w jaki sposob mozemy obejsc brak sprawdzania wersji przez hibernate, zeby  z transactional tez wyskoczyl ten blad w tescie
    }
}

