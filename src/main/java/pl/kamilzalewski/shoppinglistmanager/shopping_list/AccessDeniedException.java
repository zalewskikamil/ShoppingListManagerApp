package pl.kamilzalewski.shoppinglistmanager.shopping_list;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
