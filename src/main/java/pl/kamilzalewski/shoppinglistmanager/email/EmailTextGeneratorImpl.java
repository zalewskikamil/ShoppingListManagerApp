package pl.kamilzalewski.shoppinglistmanager.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kamilzalewski.shoppinglistmanager.jwt.access_token.AccessToken;
import pl.kamilzalewski.shoppinglistmanager.password.token.ForgotPasswordTokenService;
import pl.kamilzalewski.shoppinglistmanager.user.User;

@Component
@RequiredArgsConstructor
public class EmailTextGeneratorImpl implements EmailTextGenerator {

    private final UrlCreator urlCreator;
    private final ForgotPasswordTokenService tokenService;

    @Override
    public String generateRegisterEmailText(AccessToken accessToken) {
        return "To enable your account please click on the link below\n +" +
                urlCreator.createConfirmEmailUrl(accessToken.getToken());
    }

    @Override
    public String generateForgotPasswordEmailText(User user) {
        String token = tokenService.generateForgotPasswordToken(user);
        String url = urlCreator.createResetForgotPasswordUrl(token);
        return "To create a new password to your account please click on the link below\n" + url;
    }

    @Override
    public String generateTechnicalBreakMessageText(String startDateTime, String endDateTime) {
        return "Dear user\n" +
                "We would like to inform you about an interruption in access to the application.\n" +
                "Technical maintenance will begin at " + startDateTime + ".\nThe estimated completion time is " +
                endDateTime + ". \nWe apologize for any inconvenience.\nShopping List Manager API Team";
    }
}
