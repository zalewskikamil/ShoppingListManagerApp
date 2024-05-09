package pl.kamilzalewski.shoppinglistmanager.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.user.User;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshToken findToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("Refresh Token not found"));
    }

    public RefreshToken generateRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(jwtService.generateToken(user, 60 * 24 * 3))
                .user(user)
                .build();
        return refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken getRefreshToken(User user) {
        RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken != null) {
            try {
                jwtService.isTokenExpired(refreshToken.getToken());
                return refreshToken;
            } catch (RuntimeException e) {
                refreshTokenRepository.delete(refreshToken);
            }
        }
        return generateRefreshToken(user);
    }

    public boolean isRefreshTokenValid(RefreshToken refreshToken) {
        return jwtService.isTokenValid(refreshToken.getToken(), refreshToken.getUser());
    }

    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
