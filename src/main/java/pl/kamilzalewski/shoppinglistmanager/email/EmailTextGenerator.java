package pl.kamilzalewski.shoppinglistmanager.email;

import pl.kamilzalewski.shoppinglistmanager.jwt.access_token.AccessToken;
import pl.kamilzalewski.shoppinglistmanager.user.User;

public interface EmailTextGenerator {

    String generateRegisterEmailText(AccessToken accessToken);
    String generateForgotPasswordEmailText(User user);
    String generateTechnicalBreakMessageText(String startDateTime, String endDateTime);
}
