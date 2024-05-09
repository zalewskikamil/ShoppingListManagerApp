package pl.kamilzalewski.shoppinglistmanager.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.register(authRequest));
    }

    @GetMapping("/confirmEmail/{token}")
    public ResponseEntity<String> confirmEmail(@PathVariable String token) {
        return ResponseEntity.ok(authService.confirmEmail(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return ResponseEntity.ok(authService.refreshToken(token));
    }
}
