package pl.kamilzalewski.shoppinglistmanager.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendMessage(EmailBody emailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailBody.to());
        message.setFrom("zalewskikamil89@gmail.com");
        message.setSubject(emailBody.subject());
        message.setText(emailBody.text());
        javaMailSender.send(message);
    }

    public String createConfirmEmailUrl(String token) {
        return "http://localhost:8080/api/confirmEmail/" + token;
    }

    public String createForgotPasswordUrl(String token) {
        return "http://localhost:8080/api/forgotPassword/changePassword/" + token;
    }
}
