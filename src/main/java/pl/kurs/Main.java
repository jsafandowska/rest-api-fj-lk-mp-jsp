package pl.kurs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@EnableAsync
@SpringBootApplication
public class Main {

    public static void main(String[] args) {

         SpringApplication.run(Main.class, args);


         // stworzyc plik z blwedem, przrtestowac czy sie zapisuje blad do bazy
        // przetestowac manualnie
        // dopisac jakies testy jednostkowe

//        try (BufferedWriter out = new BufferedWriter(new FileWriter("books.csv"))) {
//            for (int i = 0; i < 30_000_000; i++) {
//                if(i == 2000000){
//                    out.write("title_" + i + "," + "," + ",LEKTURA," + 1);
//                }
//                int randomAuthor = (int) ((Math.random() * 2) + 1);
//                out.write("title_" + i + ",LEKTURA," + randomAuthor);
//                out.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
    }

}

