package pl.kamilzalewski.shoppinglistmanager.admin.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.email.EmailBody;
import pl.kamilzalewski.shoppinglistmanager.email.EmailService;
import pl.kamilzalewski.shoppinglistmanager.email.EmailTextGenerator;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailTextGenerator textGenerator;

    @Override
    public String sendTechnicalBreakMessage(String startDateTime, String endDateTime) {
        String emailText = textGenerator.generateTechnicalBreakMessageText(startDateTime, endDateTime);
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            EmailBody emailBody = EmailBody.builder()
                    .to(user.getUsername())
                    .subject("Shopping List Manager API: Technical Break Info")
                    .text(emailText)
                    .build();
            emailService.sendMessage(emailBody);
        }
        String sendMessageInfo = "The notification about the interruption has been sent to all users";
        LOGGER.info(sendMessageInfo);
        return sendMessageInfo;
    }
}
