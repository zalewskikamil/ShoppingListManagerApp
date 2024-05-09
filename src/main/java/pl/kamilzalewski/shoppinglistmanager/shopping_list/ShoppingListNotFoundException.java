package pl.kamilzalewski.shoppinglistmanager.shopping_list;

public class ShoppingListNotFoundException extends RuntimeException {

    public ShoppingListNotFoundException(String message) {
        super(message);
    }
}
