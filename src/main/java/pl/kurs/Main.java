package pl.kurs;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        // stworz dwie klasy Car(id, brand,model, String fuelType)
        // Garage(id, int places, address,  boolean lpgAllowed)
        // tworzymy nowe kontrolery i pelnego CRUDA

    }
}