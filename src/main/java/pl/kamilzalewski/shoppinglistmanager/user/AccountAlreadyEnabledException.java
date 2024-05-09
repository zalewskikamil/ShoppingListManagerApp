package pl.kamilzalewski.shoppinglistmanager.user;

public class AccountAlreadyEnabledException extends RuntimeException {
    public AccountAlreadyEnabledException(String message) {
        super(message);
    }
}
