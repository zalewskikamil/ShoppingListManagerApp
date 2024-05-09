package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import pl.kamilzalewski.shoppinglistmanager.user.UserDtoMapper;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ShoppingListDtoMapper {

    public static ShoppingListDto mapToShoppingListDto(ShoppingList shoppingList) {
        return ShoppingListDto.builder()
                .id(shoppingList.getId())
                .name(shoppingList.getName())
                .createdBy(UserDtoMapper.mapToUserDto(shoppingList.getCreatedBy()))
                .created(shoppingList.getCreated())
                .build();
    }

    public static List<ShoppingListDto> mapToShoppingListDtos(Page<ShoppingList> page) {
        List<ShoppingList> lists = page.getContent();
        List<ShoppingListDto> listDtos = new ArrayList<>();
        if (!lists.isEmpty()) {
            lists.forEach(l -> listDtos.add(mapToShoppingListDto(l)));
        }
        return listDtos;
    }
}
