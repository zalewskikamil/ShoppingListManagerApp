package pl.kamilzalewski.shoppinglistmanager.jwt.access_token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    @Query("SELECT at FROM AccessToken at WHERE at.user.id = ?1 AND at.isLoggedOut = false")
    List<AccessToken> findAllValidAccessTokensByUser(Long userId);

    Optional<AccessToken> findByToken(String token);
}
