package pl.kamilzalewski.shoppinglistmanager.password;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.email.EmailBody;
import pl.kamilzalewski.shoppinglistmanager.email.EmailService;
import pl.kamilzalewski.shoppinglistmanager.jwt.JwtService;
import pl.kamilzalewski.shoppinglistmanager.jwt.TokenValidityException;
import pl.kamilzalewski.shoppinglistmanager.jwt.TokenNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.exceptions.UserNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PasswordService.class);
    private static final long EXPIRATION_TIME_IN_MINUTES = 15;

    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public String changePassword(String authorizationHeader, ChangePassword changePassword) {
        if (!Objects.equals(changePassword.newPassword(), changePassword.repeatNewPassword())) {
            throw new IncorrectPasswordException("Please enter the new password again");
        }
        String token = authorizationHeader.substring(7);
        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(changePassword.actualPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Please enter correct password");
        }
        String encodedNewPassword = passwordEncoder.encode(changePassword.newPassword());
        userRepository.updatePassword(user.getUsername(), encodedNewPassword);
        LOGGER.info("The user with ID {} changed password", user.getId());
        return "Password has been changed";
    }

    public String sendForgotPasswordMessage(String userEmail) {
        User user = userRepository.findByUsername(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found. Please provide valid email"));
        String forgotPasswordToken = generateForgotPasswordToken(user);
        EmailBody emailBody = EmailBody.builder()
                .to(user.getUsername())
                .subject("Shopping List Manager API: Forgot Password Request")
                .text("To create a new password to your account please click on the link below\n" +
                        emailService.createForgotPasswordUrl(forgotPasswordToken))
                .build();
        emailService.sendMessage(emailBody);
        LOGGER.info("The user {} changed forgotten password", userEmail);
        return "Email send for verification. Please check your mailbox";
    }

    public String changeForgottenPassword(ChangeForgottenPasswordRequest changeForgottenPasswordRequest,
                                                          String token) {
        if (!Objects.equals(changeForgottenPasswordRequest.newPassword(), changeForgottenPasswordRequest.repeatNewPassword())) {
            throw new IncorrectPasswordException("Please enter the new password again");
        }
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByForgotPasswordToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Forgot password token not found"));
        if(jwtService.isTokenExpired(token)) {
            throw new TokenValidityException("Token has expired");
        }
        String userEmail = jwtService.extractUsername(token);
        String encodePassword = passwordEncoder.encode(changeForgottenPasswordRequest.newPassword());
        userRepository.updatePassword(userEmail, encodePassword);
        forgotPasswordTokenRepository.delete(forgotPasswordToken);
        LOGGER.info("The user {} has changed forgotten password", userEmail);
        return "Password has been changed";
    }

    private String generateForgotPasswordToken(User user) {
        Optional<ForgotPasswordToken> fpTokenByUser = forgotPasswordTokenRepository.findByUser(user);
        fpTokenByUser.ifPresent(forgotPasswordTokenRepository::delete);
        ForgotPasswordToken forgotPasswordToken = ForgotPasswordToken.builder()
                .token(jwtService.generateToken(user, EXPIRATION_TIME_IN_MINUTES))
                .build();
        ForgotPasswordToken savedForgotPasswordToken = forgotPasswordTokenRepository.save(forgotPasswordToken);
        return savedForgotPasswordToken.getToken();
    }
}
