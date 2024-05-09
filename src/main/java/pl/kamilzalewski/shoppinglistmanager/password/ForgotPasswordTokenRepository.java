package pl.kamilzalewski.shoppinglistmanager.password;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kamilzalewski.shoppinglistmanager.user.User;

import java.util.Optional;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Integer> {

    @Query("Select fp from ForgotPasswordToken fp where fp.token = ?1")
    Optional<ForgotPasswordToken> findByForgotPasswordToken(String forgotPasswordToken);

    @Query("Select fp from ForgotPasswordToken fp where fp.user = ?1")
    Optional<ForgotPasswordToken> findByUser(User user);
}
