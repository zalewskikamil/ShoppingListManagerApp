package pl.kamilzalewski.shoppinglistmanager.password.change;

import jakarta.validation.constraints.NotBlank;

public record ChangePassword(
        @NotBlank(message = "Password can't be blank")
        String actualPassword,
        @NotBlank(message = "Password can't be blank")
        String newPassword,
        @NotBlank(message = "Password can't be blank")
        String repeatNewPassword) {

}
