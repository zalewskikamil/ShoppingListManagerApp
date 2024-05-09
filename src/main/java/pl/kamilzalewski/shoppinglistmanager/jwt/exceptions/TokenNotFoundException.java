package pl.kamilzalewski.shoppinglistmanager.jwt.exceptions;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException(String message) {
        super(message);
    }
}
