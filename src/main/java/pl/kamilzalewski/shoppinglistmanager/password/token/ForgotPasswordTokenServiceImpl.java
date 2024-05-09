package pl.kamilzalewski.shoppinglistmanager.password.token;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.jwt.JwtService;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForgotPasswordTokenServiceImpl implements ForgotPasswordTokenService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordTokenServiceImpl.class);

    private final static long EXPIRATION_FORGOT_PASSWORD_TOKEN_PERIOD_IN_MILLIS = 1000 * 60 * 60;

    @PersistenceContext
    private EntityManager entityManager;
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    @Override
    public String generateForgotPasswordToken(User user) {
        Optional<ForgotPasswordToken> fpTokenByUser = forgotPasswordTokenRepository.findByUser(user);
        if (fpTokenByUser.isPresent()) {
            user.setForgotPasswordToken(null);
            user = userRepository.save(user);
            entityManager.flush();
        }
        String token = jwtService.generateToken(user, EXPIRATION_FORGOT_PASSWORD_TOKEN_PERIOD_IN_MILLIS);
        ForgotPasswordToken newForgotPasswordToken = ForgotPasswordToken.builder()
                .token(token)
                .user(user)
                .build();
        ForgotPasswordToken savedForgotPasswordToken = forgotPasswordTokenRepository.save(newForgotPasswordToken);
        return savedForgotPasswordToken.getToken();
    }
}
