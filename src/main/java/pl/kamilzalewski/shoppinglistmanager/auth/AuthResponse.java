package pl.kamilzalewski.shoppinglistmanager.auth;

public record AuthResponse(String accessToken, String refreshToken) {
}
