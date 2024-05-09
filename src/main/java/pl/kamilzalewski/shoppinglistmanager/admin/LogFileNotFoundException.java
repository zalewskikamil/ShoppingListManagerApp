package pl.kamilzalewski.shoppinglistmanager.admin;

public class LogFileNotFoundException extends RuntimeException {
    public LogFileNotFoundException(String message) {
        super(message);
    }
}
