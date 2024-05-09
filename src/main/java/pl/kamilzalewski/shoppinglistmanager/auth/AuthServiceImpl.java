package pl.kamilzalewski.shoppinglistmanager.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.auth.exceptions.AccountAlreadyEnabledException;
import pl.kamilzalewski.shoppinglistmanager.auth.exceptions.EmailAlreadyExistException;
import pl.kamilzalewski.shoppinglistmanager.email.EmailBody;
import pl.kamilzalewski.shoppinglistmanager.email.EmailService;
import pl.kamilzalewski.shoppinglistmanager.email.EmailTextGenerator;
import pl.kamilzalewski.shoppinglistmanager.jwt.access_token.AccessToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.access_token.AccessTokenService;
import pl.kamilzalewski.shoppinglistmanager.jwt.refresh_token.RefreshToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.refresh_token.RefreshTokenService;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;
import pl.kamilzalewski.shoppinglistmanager.user.UserRole;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;
    private final EmailTextGenerator textGenerator;

    @Override
    public String register(AuthRequest authRequest) {
        String userEmail = authRequest.userEmail();
        if (userRepository.findByUsername(userEmail).isPresent()) {
            throw new EmailAlreadyExistException("Email " + userEmail + " is already taken");
        }
        User user = new User();
        user.setUsername(userEmail);
        user.setPassword(passwordEncoder.encode(authRequest.password()));
        user.setRole(UserRole.USER);
        User savedUser = userRepository.save(user);
        AccessToken accessToken = accessTokenService.generateAccessToken(savedUser);
        EmailBody emailBody = EmailBody.builder()
                .to(savedUser.getUsername())
                .subject("Shopping List Manager API - verification link")
                .text(textGenerator.generateRegisterEmailText(accessToken))
                .build();
        emailService.sendMessage(emailBody);
        LOGGER.info("A new user {} has been registered", userEmail);
        return "Email send for verification. Please check your mailbox";
    }

    @Override
    public String confirmEmail(String token) {
        AccessToken accessToken = accessTokenService.findAccessToken(token);
        if (accessToken.isLoggedOut()) {
            throw new AccountAlreadyEnabledException("Account already enabled");
        }
        User user = accessToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        accessTokenService.revokeAccessToken(accessToken);
        LOGGER.info("The user {} has enabled their account", user.getId());
        return "Account successfully enabled!";
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.userEmail(), authRequest.password()));
        User user = userRepository.findByUsername(authRequest.userEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        AccessToken accessToken = accessTokenService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.getValidRefreshToken(user);
        LOGGER.info("The user with ID {} has logged in", user.getId());
        return new AuthResponse(accessToken.getToken(), refreshToken.getToken());
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        RefreshToken refreshTokenFromDB = refreshTokenService.findRefreshToken(refreshToken);
        if (!refreshTokenService.isRefreshTokenValid(refreshTokenFromDB)) {
            throw new RuntimeException("Refresh Token expired. Please sign in");
        }
        User user = refreshTokenFromDB.getUser();
        accessTokenService.revokeAllAccessTokensByUser(user);
        AccessToken newAccessToken = accessTokenService.generateAccessToken(user);
        RefreshToken newRefreshToken = refreshTokenService.generateNewRefreshToken(user);
        LOGGER.info("The user with ID {} refreshed token", user.getId());
        return new AuthResponse(newAccessToken.getToken(), newRefreshToken.getToken());
    }
}