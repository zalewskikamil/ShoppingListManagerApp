package pl.kamilzalewski.shoppinglistmanager.item;

import jakarta.validation.constraints.NotBlank;

public record ItemRequest(
        @NotBlank(message = "Item's name can't be blank")
        String name,
        double quantity,
        String unit,
        String description,
        boolean isBought) {
}
