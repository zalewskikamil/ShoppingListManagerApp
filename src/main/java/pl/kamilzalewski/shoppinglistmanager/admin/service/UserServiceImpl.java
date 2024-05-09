package pl.kamilzalewski.shoppinglistmanager.admin.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;
import pl.kamilzalewski.shoppinglistmanager.user.UserRole;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public String changeUserBlockedStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        boolean blockedStatusToChange = user.isBlocked();
        user.setBlocked(!blockedStatusToChange);
        userRepository.save(user);
        String infoAboutChange = blockedStatusToChange ?
                "User with ID " + userId + " has been unlocked" : "User with ID " + userId + " has been blocked";
        LOGGER.info(infoAboutChange);
        return infoAboutChange;
    }

    @Override
    public String grantAdminStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        if (user.getRole() == UserRole.ADMIN) {
            return "User with ID " + userId + " already has ADMIN role";
        }
        user.setRole(UserRole.ADMIN);
        userRepository.save(user);
        String adminInfo = "User with ID " + userId + " is a new ADMIN";
        LOGGER.info(adminInfo);
        return adminInfo;
    }
}
