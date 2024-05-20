package pl.kurs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kurs.model.Book;
import pl.kurs.model.Garage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class GarageConfiguration {
    @Bean
    public List<Garage> garages(){
        return  Collections.synchronizedList(new ArrayList<>());
    }
}
