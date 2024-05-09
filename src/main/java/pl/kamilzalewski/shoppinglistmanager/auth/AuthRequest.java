package pl.kamilzalewski.shoppinglistmanager.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "The username filed can't be blank")
        @Email(message = "Please enter email in proper format")
        String userEmail,

        @NotBlank(message = "The password filed can't be blank")
        String password
) {
}
