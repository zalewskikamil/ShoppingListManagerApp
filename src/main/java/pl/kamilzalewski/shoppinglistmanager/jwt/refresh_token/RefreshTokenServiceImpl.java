package pl.kamilzalewski.shoppinglistmanager.jwt.refresh_token;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.jwt.JwtService;
import pl.kamilzalewski.shoppinglistmanager.jwt.exceptions.TokenNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final static long EXPIRATION_REFRESH_TOKEN_PERIOD_IN_MILLIS = 1000 * 60 * 60 * 24 * 7;

    @PersistenceContext
    private EntityManager entityManager;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("Refresh Token not found"));
    }

    @Override
    public boolean isRefreshTokenValid(RefreshToken refreshToken) {
        return jwtService.isTokenValid(refreshToken.getToken(), refreshToken.getUser());
    }

    @Transactional
    @Override
    public RefreshToken getValidRefreshToken(User user) {
        RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken != null && isRefreshTokenValid(refreshToken)) {
            return refreshToken;
        }
        return generateNewRefreshToken(user);
    }

    @Transactional
    @Override
    public RefreshToken generateNewRefreshToken(User user) {
        RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken != null) {
            deleteRefreshToken(refreshToken);
            entityManager.flush();
        }
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(jwtService.generateToken(user, EXPIRATION_REFRESH_TOKEN_PERIOD_IN_MILLIS))
                .user(user)
                .build();
        return refreshTokenRepository.save(newRefreshToken);
    }

    @Transactional
    @Override
    public void deleteRefreshToken(RefreshToken refreshToken) {
        User user = refreshToken.getUser();
        user.setRefreshToken(null);
        userRepository.save(user);
        refreshTokenRepository.deleteById(refreshToken.getId());
    }
}