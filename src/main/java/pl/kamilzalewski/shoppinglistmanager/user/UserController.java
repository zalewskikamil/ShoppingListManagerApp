package pl.kamilzalewski.shoppinglistmanager.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.register(userRequest));
    }

    @GetMapping("/confirmEmail/{token}")
    public ResponseEntity<UserResponse> confirmEmailHandler(@PathVariable String token) {
        return ResponseEntity.ok(userService.confirmEmail(token));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.login(userRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserResponse> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.refreshToken(authorizationHeader));
    }
}
