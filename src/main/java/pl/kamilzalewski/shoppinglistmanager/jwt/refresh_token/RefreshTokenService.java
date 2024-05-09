package pl.kamilzalewski.shoppinglistmanager.jwt.refresh_token;

import pl.kamilzalewski.shoppinglistmanager.user.User;

public interface RefreshTokenService {

    RefreshToken findRefreshToken(String refreshToken);

    RefreshToken generateNewRefreshToken(User user);

    RefreshToken getValidRefreshToken(User user);

    boolean isRefreshTokenValid(RefreshToken refreshToken);

    void deleteRefreshToken(RefreshToken refreshToken);
}