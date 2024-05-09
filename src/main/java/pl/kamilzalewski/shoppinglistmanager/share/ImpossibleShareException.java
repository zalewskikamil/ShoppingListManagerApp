package pl.kamilzalewski.shoppinglistmanager.share;

public class ImpossibleShareException extends RuntimeException {
    public ImpossibleShareException(String message) {
        super(message);
    }
}
