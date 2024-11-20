package pl.kurs.exceptions;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException() {
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
