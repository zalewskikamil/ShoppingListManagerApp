package pl.kamilzalewski.shoppinglistmanager.auth;

public interface AuthService {

    String register(AuthRequest authRequest);

    String confirmEmail(String accessToken);

    AuthResponse login(AuthRequest authRequest);

    AuthResponse refreshToken(String token);
}
