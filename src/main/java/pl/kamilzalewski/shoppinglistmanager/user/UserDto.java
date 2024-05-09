package pl.kamilzalewski.shoppinglistmanager.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private Long id;

    private String username;
}
