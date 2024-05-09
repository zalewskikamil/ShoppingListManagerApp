package pl.kamilzalewski.shoppinglistmanager.password.token;

import pl.kamilzalewski.shoppinglistmanager.user.User;

public interface ForgotPasswordTokenService {

    String generateForgotPasswordToken(User user);
}
