package pl.kamilzalewski.shoppinglistmanager.password.change;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
