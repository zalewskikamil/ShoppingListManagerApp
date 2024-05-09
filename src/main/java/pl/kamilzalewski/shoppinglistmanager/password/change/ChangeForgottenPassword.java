package pl.kamilzalewski.shoppinglistmanager.password.change;

import jakarta.validation.constraints.NotBlank;

public record ChangeForgottenPassword(
        @NotBlank(message = "Password can't be blank")
        String newPassword,
        @NotBlank(message = "Password can't be blank")
        String repeatNewPassword) {
}
