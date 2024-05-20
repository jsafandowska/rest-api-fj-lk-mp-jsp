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
    public List<Car> cars(){
        return  Collections.synchronizedList(new ArrayList<>());
    }
}
