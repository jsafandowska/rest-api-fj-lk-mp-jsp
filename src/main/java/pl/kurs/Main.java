package pl.kurs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableAsync
@SpringBootApplication
public class  Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("admin"));

        // todo doczytac jak dzialają pozostale creationPolicy stateless etc
        // co to jest csrf
        // z czego sie sklada JWT

        // Api do tworzenia userow i ról
        /*
        tworzenie usera, tworzenie roli, przypisawanie roli do usera, pobieranie usera z repozytorium
        user: id, name, surname, email, password
        role: id, name
        many-to-many
        zmien konfiguracje spring security tak aby userDetailService pobierał userow z bazy a nie z pamieci
        dodac odpowiednie tabele w liquibase, dane inicjalne itp
         */


    }
}

