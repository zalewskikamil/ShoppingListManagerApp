package pl.kamilzalewski.shoppinglistmanager.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequest {

    @NotBlank(message = "The username filed can't be blank")
    @Email(message = "Please enter email in proper format")
    private String userEmail;

    @NotBlank(message = "The password filed can't be blank")
    private String password;
}
