package pl.kamilzalewski.shoppinglistmanager.jwt.access_token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.jwt.JwtService;
import pl.kamilzalewski.shoppinglistmanager.jwt.exceptions.TokenNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService{
    private final static long EXPIRATION_ACCESS_TOKEN_PERIOD_IN_MILLIS = 1000 * 60 * 60 * 24;

    private final AccessTokenRepository accessTokenRepository;
    private final JwtService jwtService;

    @Override
    public AccessToken findAccessToken(String accessToken) {
        return accessTokenRepository.findByToken(accessToken)
                .orElseThrow(() -> new TokenNotFoundException("Access Token not found"));
    }

    @Override
    public AccessToken generateAccessToken(User user) {
        AccessToken accessToken =  AccessToken.builder()
                .token(jwtService.generateToken(user, EXPIRATION_ACCESS_TOKEN_PERIOD_IN_MILLIS))
                .isLoggedOut(false)
                .user(user)
                .build();
        return accessTokenRepository.save(accessToken);
    }

    @Override
    public void revokeAccessToken(AccessToken token) {
        token.setLoggedOut(true);
        accessTokenRepository.save(token);
    }

    @Override
    public void revokeAllAccessTokensByUser(User user) {
        List<AccessToken> tokens = accessTokenRepository.findAllValidAccessTokensByUser(user.getId());
        if (tokens.isEmpty()) {
            return;
        }
        tokens.forEach(token ->  {
            token.setLoggedOut(true);
        });
        accessTokenRepository.saveAll(tokens);
    }
}