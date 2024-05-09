package pl.kamilzalewski.shoppinglistmanager.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final JwtService jwtService;

    public AccessToken findAccessToken(String accessToken) {
        return accessTokenRepository.findByToken(accessToken)
                .orElseThrow(() -> new TokenNotFoundException("Access Token not found"));
    }
    public AccessToken generateAccessToken(User user) {
         AccessToken accessToken =  AccessToken.builder()
                .token(jwtService.generateToken(user))
                .isLoggedOut(false)
                .user(user)
                .build();
         return accessTokenRepository.save(accessToken);
    }

    public void revokeToken(AccessToken token) {
        AccessToken tokenToRevoke = AccessToken.builder()
                .id(token.getId())
                .token(token.getToken())
                .isLoggedOut(true)
                .user(token.getUser())
                .build();
        accessTokenRepository.save(tokenToRevoke);
    }

    public void revokeAllTokenByUser(User user) {
        List<AccessToken> validTokens = accessTokenRepository.findAllValidAccessTokensByUser(user.getId());
        List<AccessToken> invalidatedTokens = new ArrayList<>();
        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(token ->  {
            AccessToken invalidatedToken = AccessToken.builder()
                            .id(token.getId())
                            .token(token.getToken())
                            .isLoggedOut(true)
                            .user(token.getUser())
                            .build();
            invalidatedTokens.add(invalidatedToken);
        });
        accessTokenRepository.saveAll(invalidatedTokens);
    }
}
