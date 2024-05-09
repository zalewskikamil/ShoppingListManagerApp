package pl.kamilzalewski.shoppinglistmanager.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserDtoMapper {

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
