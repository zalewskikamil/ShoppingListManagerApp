package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

public class ShoppingListNotFoundException extends RuntimeException {

    public ShoppingListNotFoundException(String message) {
        super(message);
    }
}
