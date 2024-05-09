package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import jakarta.validation.constraints.NotBlank;

public record ShoppingListRequest(
        @NotBlank(message = "Shopping list's name can't be blank")
        String name) {
}
