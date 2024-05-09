package pl.kamilzalewski.shoppinglistmanager.jwt.exceptions;

public class TokenValidityException extends RuntimeException {
    public TokenValidityException(String message) {
        super(message);
    }
}
