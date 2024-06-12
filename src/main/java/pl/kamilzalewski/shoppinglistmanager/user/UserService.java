package pl.kamilzalewski.shoppinglistmanager.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.email.EmailBody;
import pl.kamilzalewski.shoppinglistmanager.email.EmailService;
import pl.kamilzalewski.shoppinglistmanager.jwt.AccessToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.AccessTokenService;
import pl.kamilzalewski.shoppinglistmanager.jwt.RefreshToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.RefreshTokenService;
import pl.kamilzalewski.shoppinglistmanager.user.exceptions.AccountAlreadyEnabledException;
import pl.kamilzalewski.shoppinglistmanager.user.exceptions.UserAlreadyExistException;
import pl.kamilzalewski.shoppinglistmanager.user.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final EmailService emailService;


    public String register(UserRequest userRequest) {
        String userEmail = userRequest.getUserEmail();
        if (userRepository.findByUsername(userEmail).isPresent()) {
            throw new UserAlreadyExistException("User with email " + userEmail + " already exist");
        }
        User user = User.builder()
                .username(userEmail)
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(UserRole.USER)
                .build();
        User savedUser = userRepository.save(user);
        AccessToken accessToken = accessTokenService.generateAccessToken(savedUser);
        String token = accessToken.getToken();
        EmailBody emailBody = EmailBody.builder()
                .to(savedUser.getUsername())
                .subject("Shopping List Manager API - verification link")
                .text("To enable your account please click on the link below\n +" +
                        emailService.createConfirmEmailUrl(token))
                .build();
        emailService.sendMessage(emailBody);
        LOGGER.info("A new user {} has been registered", userEmail);
        return "Email send for verification. Please check your mailbox";
    }

    public UserResponse confirmEmail(String token) {
        AccessToken accessToken = accessTokenService.findAccessToken(token);
        if (accessToken.isLoggedOut()) {
            throw new AccountAlreadyEnabledException("Account already enabled");
        }
        User user = accessToken.getUser();
        User userToSave = User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .isEnabled(true)
                .isBlocked(user.isBlocked())
                .tokens(user.getTokens())
                .shoppingLists(user.getShoppingLists())
                .sharesLists(user.getSharesLists())
                .build();
        User savedUser = userRepository.save(userToSave);
        LOGGER.info("The user {} has enabled their account", user.getId());
        accessTokenService.revokeToken(accessToken);
        AccessToken newAccessToken = accessTokenService.generateAccessToken(savedUser);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(savedUser);
        return UserResponse.builder()
                .accessToken(newAccessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public UserResponse login(UserRequest userRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUserEmail(), userRequest.getPassword()));
        User user = userRepository.findByUsername(userRequest.getUserEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        AccessToken accessToken = accessTokenService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(user);
        LOGGER.info("The user with ID {} has logged in", user.getId());
        return UserResponse.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public UserResponse refreshToken(String authorizationHeader) {
        String refreshToken = authorizationHeader.substring(7);
        RefreshToken refreshTokenFromDB = refreshTokenService.findToken(refreshToken);
        if (!refreshTokenService.isRefreshTokenValid(refreshTokenFromDB)) {
            throw new RuntimeException("Refresh Token expired. Please sign in");
        }
        User user = refreshTokenFromDB.getUser();
        accessTokenService.revokeAllTokenByUser(user);
        AccessToken accessToken = accessTokenService.generateAccessToken(user);
        return UserResponse.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken)
                .build();
    }
}
