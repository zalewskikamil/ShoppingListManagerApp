package pl.kamilzalewski.shoppinglistmanager.password;

import jakarta.validation.constraints.NotBlank;

public record ChangeForgottenPasswordRequest(
        @NotBlank(message = "Password can't be blank")
        String newPassword,
        @NotBlank(message = "Password can't be blank")
        String repeatNewPassword) {
}