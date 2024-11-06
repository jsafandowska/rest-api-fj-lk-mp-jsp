package pl.kurs.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolation;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(exc.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList()));
    }

    public record ErrorDto(List<String> messages) {
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exc) {
        List<String> errors = exc.getConstraintViolations().stream()
                                 .map(ConstraintViolation::getMessage)
                                 .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(errors));
    }

}
