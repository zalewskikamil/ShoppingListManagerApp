package pl.kamilzalewski.shoppinglistmanager.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("$(spring.mail.username)")
    private String emailSender;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMessage(EmailBody emailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailBody.to());
        message.setFrom(emailSender);
        message.setSubject(emailBody.subject());
        message.setText(emailBody.text());
        javaMailSender.send(message);
    }
}
