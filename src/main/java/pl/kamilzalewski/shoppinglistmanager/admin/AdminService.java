package pl.kamilzalewski.shoppinglistmanager.admin;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.email.EmailBody;
import pl.kamilzalewski.shoppinglistmanager.email.EmailService;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.exceptions.UserNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;
import pl.kamilzalewski.shoppinglistmanager.user.UserRole;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    private static final String LOG_FILE_PATH = "logs/app.log";

    private final UserRepository userRepository;
    private final EmailService emailService;

    public Resource getLogFile() {
        File file = new File(LOG_FILE_PATH);
        if (!file.exists()) {
            throw new LogFileNotFoundException("Log file not found");
        }
        LOGGER.info("The admin has downloaded the log file");
        return new FileSystemResource(file);
    }

    public String changeBlockedStatus(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        User savedUser = userRepository.save(User.builder()
                .id(userId)
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .isEnabled(user.isEnabled())
                .isBlocked(!user.isBlocked())
                .tokens(user.getTokens())
                .shoppingLists(user.getShoppingLists())
                .sharesLists(user.getSharesLists())
                .build());
        boolean isAccountBlockedAlready = savedUser.isBlocked();
        String infoAboutChange = isAccountBlockedAlready ?
                "User with ID " + userId + " has been blocked" : "User with ID " + userId + " has been unlocked";
        LOGGER.info(infoAboutChange);
        return infoAboutChange;
    }

    public String giveAdminStatusToUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    if (user.getRole() == UserRole.ADMIN) {
        return "User with ID " + userId + " already has ADMIN role";
    }
    userRepository.save(User.builder()
            .id(userId)
            .username(user.getUsername())
            .password(user.getPassword())
            .role(UserRole.ADMIN)
            .isEnabled(user.isEnabled())
            .isBlocked(user.isBlocked())
            .tokens(user.getTokens())
            .shoppingLists(user.getShoppingLists())
            .sharesLists(user.getSharesLists())
            .build());
    String adminInfo = "User with ID " + userId + " is a new ADMIN";
    LOGGER.info(adminInfo);
    return adminInfo;
    }

    public String sendTechnicalBreakMessage(String startDate, String endDate) {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            EmailBody emailBody = EmailBody.builder()
                    .to(user.getUsername())
                    .subject("Shopping List Manager API: Technical Break Info")
                    .text(("Dear user\n" +
                            "We would like to inform you about an interruption in access to the application.\n" +
                            "Technical maintenance will begin at " + startDate + ".\n The estimated completion time is" +
                             endDate + " We apologize for any inconvenience.\nShopping List Manager API Team"))
                    .build();
            emailService.sendMessage(emailBody);
        }
        return "The notification about the interruption has been sent to all users";
    }


}
