package pl.kamilzalewski.shoppinglistmanager.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDto {

    private Long id;

    private String name;

    private double quantity;

    private String unit;

    private String description;

    private boolean bought;
}
