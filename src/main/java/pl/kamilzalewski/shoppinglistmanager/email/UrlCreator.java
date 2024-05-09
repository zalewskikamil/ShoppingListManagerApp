package pl.kamilzalewski.shoppinglistmanager.email;

public interface UrlCreator {
    String createConfirmEmailUrl(String token);
    String createResetForgotPasswordUrl(String token);
}
