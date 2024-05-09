package pl.kamilzalewski.shoppinglistmanager.jwt;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException(String message) {
        super(message);
    }
}
