package pl.kamilzalewski.shoppinglistmanager.password.change;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.auth.CurrentUserService;
import pl.kamilzalewski.shoppinglistmanager.jwt.JwtService;
import pl.kamilzalewski.shoppinglistmanager.jwt.exceptions.TokenNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.jwt.exceptions.TokenValidityException;
import pl.kamilzalewski.shoppinglistmanager.password.token.ForgotPasswordToken;
import pl.kamilzalewski.shoppinglistmanager.password.token.ForgotPasswordTokenRepository;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChangePasswordServiceImpl.class);

    private final ForgotPasswordTokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Transactional
    @Override
    public String changePassword(ChangePassword changePassword) {
        String newPassword = changePassword.newPassword();
        if (!Objects.equals(newPassword, changePassword.repeatNewPassword())) {
            throw new IncorrectPasswordException("The given passwords are not identical");
        }
        User user = currentUserService.getCurrentUser();
        if (!passwordEncoder.matches(changePassword.actualPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Please enter correct password");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        LOGGER.info("The user with ID {} changed password", user.getId());
        return "Password has been changed";
    }

    @Override
    public String changeForgottenPassword(String token, ChangeForgottenPassword changeForgottenPassword) {
        String newPassword = changeForgottenPassword.newPassword();
        if (!Objects.equals(newPassword, changeForgottenPassword.repeatNewPassword())) {
            throw new IncorrectPasswordException("Passwords do not match. Please enter the new password again.");
        }
        if (jwtService.isTokenExpired(token)) {
            throw new TokenValidityException("Token has expired");
        }
        ForgotPasswordToken forgotPasswordToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Forgot password token not found"));
        User user = forgotPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setForgotPasswordToken(null);
        userRepository.save(user);
        LOGGER.info("The user {} has changed forgotten password", user.getUsername());
        return "Password has been changed";
    }
}
