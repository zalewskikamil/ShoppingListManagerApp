package pl.kamilzalewski.shoppinglistmanager.share;

public class ShareNotFoundException extends RuntimeException {
    public ShareNotFoundException(String message) {
        super(message);
    }
}
