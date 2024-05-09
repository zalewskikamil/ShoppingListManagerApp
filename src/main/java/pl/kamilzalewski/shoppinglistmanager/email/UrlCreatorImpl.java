package pl.kamilzalewski.shoppinglistmanager.email;

import org.springframework.stereotype.Component;

@Component
public class UrlCreatorImpl implements UrlCreator {

    @Override
    public String createConfirmEmailUrl(String token) {
        return "http://localhost:8080/api/confirmEmail/" + token;
    }

    @Override
    public String createResetForgotPasswordUrl(String token) {
        return "http://localhost:8080/api/forgotPassword/changePassword/" + token;
    }
}
