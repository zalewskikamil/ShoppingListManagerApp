package pl.kamilzalewski.shoppinglistmanager.jwt;

public class TokenValidityException extends RuntimeException {
    public TokenValidityException(String message) {
        super(message);
    }
}
