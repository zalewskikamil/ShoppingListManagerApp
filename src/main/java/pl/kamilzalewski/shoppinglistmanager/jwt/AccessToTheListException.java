package pl.kamilzalewski.shoppinglistmanager.jwt;

public class AccessToTheListException extends RuntimeException {
    public AccessToTheListException(String message) {
        super(message);
    }
}
