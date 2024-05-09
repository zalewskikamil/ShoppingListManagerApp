package pl.kamilzalewski.shoppinglistmanager.auth.exceptions;

public class AccountAlreadyEnabledException extends RuntimeException {
    public AccountAlreadyEnabledException(String message) {
        super(message);
    }
}
