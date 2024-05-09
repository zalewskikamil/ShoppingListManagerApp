package pl.kamilzalewski.shoppinglistmanager.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDto {

    private Integer id;

    private String name;

    private double quantity;

    private String unit;

    private String description;

    @JsonProperty("isBought")
    private boolean isBought;
}
