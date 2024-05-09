package pl.kamilzalewski.shoppinglistmanager.shopping_list;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import pl.kamilzalewski.shoppinglistmanager.user.UserDtoMapper;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ShoppingListDtoMapper {

    public static List<ShoppingListDto> mapToListsDtos(Page<ShoppingList> page) {
        List<ShoppingList> shoppingLists = page.getContent();
        List<ShoppingListDto> shoppingListDtos = new ArrayList<>();
        if (!shoppingLists.isEmpty()) {
            shoppingLists.forEach(sl -> shoppingListDtos.add(mapToListDto(sl)));
        }
        return shoppingListDtos;
    }

    public static ShoppingListDto mapToListDto(ShoppingList shoppingList) {
        return ShoppingListDto.builder()
                .id(shoppingList.getId())
                .name(shoppingList.getName())
                .createdBy(UserDtoMapper.mapToUserDto(shoppingList.getCreatedBy()))
                .created(shoppingList.getCreated())
                .build();
    }
}
