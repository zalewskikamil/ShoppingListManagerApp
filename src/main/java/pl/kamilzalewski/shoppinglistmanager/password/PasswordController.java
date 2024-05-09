package pl.kamilzalewski.shoppinglistmanager.password;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/forgotPassword/{userEmail}")
    public ResponseEntity<String> sentForgotPasswordMessageHandler(@PathVariable String userEmail) {
        return ResponseEntity.ok(passwordService.sendForgotPasswordMessage(userEmail));
    }

    @PatchMapping("/forgotPassword/changePassword/{token}")
    public ResponseEntity<String> changeForgottenPasswordHandler(
            @Valid @RequestBody ChangeForgottenPasswordRequest changeForgottenPasswordRequest,
            @PathVariable String token) {
        return ResponseEntity.ok(passwordService.changeForgottenPassword(changeForgottenPasswordRequest, token));
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<String> changePasswordHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                        @Valid @RequestBody ChangePassword changePassword) {
        return ResponseEntity.ok(passwordService.changePassword(authorizationHeader, changePassword));
    }

}
