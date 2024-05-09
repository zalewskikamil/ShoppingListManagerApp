package pl.kamilzalewski.shoppinglistmanager.jwt.access_token;

import pl.kamilzalewski.shoppinglistmanager.user.User;

public interface AccessTokenService {

    AccessToken findAccessToken(String accessToken);

    AccessToken generateAccessToken(User user);

    void revokeAccessToken(AccessToken token);

    void revokeAllAccessTokensByUser(User user);
}