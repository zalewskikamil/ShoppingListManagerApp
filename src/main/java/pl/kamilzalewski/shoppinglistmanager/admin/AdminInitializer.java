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
    public void run(String... args) throws Exception {
        if (!environment.acceptsProfiles(Profiles.of("test"))) {
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = User.builder()
                        .username(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .role(UserRole.ADMIN)
                        .isEnabled(true)
                        .build();
                userRepository.save(admin);
                LOGGER.info("The first ADMIN account has been created");
            }
        }
    }
}
