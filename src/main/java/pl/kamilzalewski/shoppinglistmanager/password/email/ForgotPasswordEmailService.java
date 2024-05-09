package pl.kamilzalewski.shoppinglistmanager.password.email;

public interface ForgotPasswordEmailService {

    String sendForgotPasswordMessage(String userEmail);
}
