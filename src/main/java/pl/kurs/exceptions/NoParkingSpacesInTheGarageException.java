package pl.kurs.exceptions;

public class NoParkingSpacesInTheGarageException extends RuntimeException{
    public NoParkingSpacesInTheGarageException() {
    }

    public NoParkingSpacesInTheGarageException(String message) {
        super(message);
    }
}
