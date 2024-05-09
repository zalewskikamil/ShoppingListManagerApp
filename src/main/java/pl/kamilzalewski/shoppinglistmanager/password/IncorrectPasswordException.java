package pl.kamilzalewski.shoppinglistmanager.password;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
