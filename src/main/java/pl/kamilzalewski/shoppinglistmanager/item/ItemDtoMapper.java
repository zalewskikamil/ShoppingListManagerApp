package pl.kamilzalewski.shoppinglistmanager.item;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ItemDtoMapper {

    public static List<ItemDto> mapToItemDtos(Page<Item> page) {
        List<Item> items = page.getContent();
        List<ItemDto> itemDtos = new ArrayList<>();
        if (!items.isEmpty()) {
            items.forEach(i -> itemDtos.add(mapToItemDto(i)));
        }
        return itemDtos;
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .description(item.getDescription())
                .bought(item.isBought())
                .build();
    }
}
