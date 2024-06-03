package pl.kurs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kurs.model.Book;
import pl.kurs.model.Car;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class CarConfiguration {
    @Bean
<<<<<<<< HEAD:src/main/java/pl/kurs/configuration/BookConfiguration.java
    public List<Book> books() {
        return Collections.synchronizedList(new ArrayList<>());
========
    public List<Car> cars(){
        return  Collections.synchronizedList(new ArrayList<>());
>>>>>>>> bc9555e9adaba7a683d6628079a4c620b657017b:src/main/java/pl/kurs/configuration/CarConfiguration.java
    }
}
