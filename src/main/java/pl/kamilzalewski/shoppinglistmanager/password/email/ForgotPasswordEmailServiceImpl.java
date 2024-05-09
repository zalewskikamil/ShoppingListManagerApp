package pl.kamilzalewski.shoppinglistmanager.password.email;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.email.EmailBody;
import pl.kamilzalewski.shoppinglistmanager.email.EmailService;
import pl.kamilzalewski.shoppinglistmanager.email.EmailTextGenerator;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

@Service
@RequiredArgsConstructor
public class ForgotPasswordEmailServiceImpl implements ForgotPasswordEmailService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordEmailServiceImpl.class);

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailTextGenerator textGenerator;

    @Transactional
    @Override
    public String sendForgotPasswordMessage(String userEmail) {
        User user = userRepository.findByUsername(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found. Please provide valid email"));
        String emailSubject  = "Shopping List Manager API: Forgot Password Request";
        String emailText = textGenerator.generateForgotPasswordEmailText(user);
        EmailBody emailBody = new EmailBody(userEmail, emailSubject, emailText);
        emailService.sendMessage(emailBody);
        LOGGER.info("The user {} sent forgotten password message", userEmail);
        return "Email send for verification. Please check your mailbox";
    }
}
