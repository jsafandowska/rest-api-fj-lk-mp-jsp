package pl.kurs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DictionaryNotFoundException extends RuntimeException{

    public DictionaryNotFoundException(String message) {
        super(message);
    }
}
