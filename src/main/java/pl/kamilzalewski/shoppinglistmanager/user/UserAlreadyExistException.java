package pl.kamilzalewski.shoppinglistmanager.user;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
