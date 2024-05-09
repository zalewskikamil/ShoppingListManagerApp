package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import lombok.Builder;
import lombok.Getter;
import pl.kamilzalewski.shoppinglistmanager.user.UserDto;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShoppingListDto {
    private Integer id;

    private String name;

    private UserDto createdBy;

    private LocalDateTime created;
}
