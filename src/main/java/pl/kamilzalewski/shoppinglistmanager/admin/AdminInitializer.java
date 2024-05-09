package pl.kamilzalewski.shoppinglistmanager.admin;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;
import pl.kamilzalewski.shoppinglistmanager.user.UserRole;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;
    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (environment.acceptsProfiles(Profiles.of("prod"))) {
            Optional<User> optionalAdmin = userRepository.findByUsername(adminUsername);
            if (optionalAdmin.isPresent()) {
                return;
            }
            User firstAdmin = new User();
            firstAdmin.setUsername(adminPassword);
            firstAdmin.setPassword(passwordEncoder.encode(adminPassword));
            firstAdmin.setRole(UserRole.ADMIN);
            firstAdmin.setEnabled(true);
            userRepository.save(firstAdmin);
            LOGGER.info("The first ADMIN account has been created");
        }
    }
}