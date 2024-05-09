package pl.kamilzalewski.shoppinglistmanager.password.token;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamilzalewski.shoppinglistmanager.user.User;

import java.util.Optional;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {
    Optional<ForgotPasswordToken> findByToken(String token);

    Optional<ForgotPasswordToken> findByUser(User user);
}
