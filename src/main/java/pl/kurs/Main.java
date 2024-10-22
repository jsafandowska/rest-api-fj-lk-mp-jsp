package pl.kurs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class  Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        // todo sprawdzic w jaki sposob przechowywac dane w properties
        // dodac nowe adnotacje do uzupelnienia swaggera, co znajdziecie im wiecej tym lepiej, nie wszedzie zeby nie zasmiecac, w garage controller po prostu

    }
}

