package pl.kurs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kurs.model.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class BookConfiguration {

    @Bean
    public List<Book> books(){
        return  Collections.synchronizedList(new ArrayList<>());
    }
}
