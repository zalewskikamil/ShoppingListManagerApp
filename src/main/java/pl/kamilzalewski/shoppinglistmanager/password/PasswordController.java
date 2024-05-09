package pl.kamilzalewski.shoppinglistmanager.password;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kamilzalewski.shoppinglistmanager.password.change.ChangeForgottenPassword;
import pl.kamilzalewski.shoppinglistmanager.password.change.ChangePassword;
import pl.kamilzalewski.shoppinglistmanager.password.change.ChangePasswordService;
import pl.kamilzalewski.shoppinglistmanager.password.email.ForgotPasswordEmailService;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PasswordController {

    private final ChangePasswordService changePasswordService;
    private final ForgotPasswordEmailService forgotPasswordEmailService;

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePassword changePassword) {
        return ResponseEntity.ok(changePasswordService.changePassword(changePassword));
    }

    @PostMapping("/forgotPassword/changePassword/{token}")
    public ResponseEntity<String> changeForgottenPassword
            (@PathVariable String token,
             @Valid @RequestBody ChangeForgottenPassword changeForgottenPassword) {
        return ResponseEntity.ok(changePasswordService.changeForgottenPassword(token, changeForgottenPassword));
    }

    @PostMapping("/forgotPassword/{userEmail}")
    public ResponseEntity<String> sentForgotPasswordMessage(@PathVariable String userEmail) {
        return ResponseEntity.ok(forgotPasswordEmailService.sendForgotPasswordMessage(userEmail));
    }
}
