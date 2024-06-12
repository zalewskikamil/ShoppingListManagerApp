package pl.kamilzalewski.shoppinglistmanager.user.exceptions;

public class AccountAlreadyEnabledException extends RuntimeException {
    public AccountAlreadyEnabledException(String message) {
        super(message);
    }
}
