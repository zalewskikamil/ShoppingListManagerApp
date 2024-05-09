package pl.kamilzalewski.shoppinglistmanager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import pl.kamilzalewski.shoppinglistmanager.jwt.access_token.AccessToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.access_token.AccessTokenService;
import pl.kamilzalewski.shoppinglistmanager.jwt.refresh_token.RefreshToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.refresh_token.RefreshTokenService;
import pl.kamilzalewski.shoppinglistmanager.user.User;

@Configuration
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLogoutHandler.class);

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String token = authHeader.substring(7);
        AccessToken accessToken = accessTokenService.findAccessToken(token);
        User user = accessToken.getUser();
        accessTokenService.revokeAllAccessTokensByUser(user);
        RefreshToken refreshToken = user.getRefreshToken();
        refreshTokenService.deleteRefreshToken(refreshToken);
        LOGGER.info("The user with ID {} logged out", user.getId());
        request.getSession().invalidate();
    }
}
