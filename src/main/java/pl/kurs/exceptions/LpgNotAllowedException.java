package pl.kurs.exceptions;

public class LpgNotAllowedException extends RuntimeException{
    public LpgNotAllowedException() {
    }

    public LpgNotAllowedException(String message) {
        super(message);
    }
}
