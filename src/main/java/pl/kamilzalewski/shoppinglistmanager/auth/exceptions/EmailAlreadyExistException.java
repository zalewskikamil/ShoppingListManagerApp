package pl.kamilzalewski.shoppinglistmanager.auth.exceptions;

public class EmailAlreadyExistException extends RuntimeException {

    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
