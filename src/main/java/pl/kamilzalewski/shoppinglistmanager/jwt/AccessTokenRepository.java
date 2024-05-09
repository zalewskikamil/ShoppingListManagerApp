package pl.kamilzalewski.shoppinglistmanager.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {

    @Query("Select at from AccessToken at inner join User u on at.user.id = u.id " +
            "where at.user.id = ?1 and at.isLoggedOut = false")
    List<AccessToken> findAllValidAccessTokensByUser(Integer userId);

    Optional<AccessToken> findByToken(String token);
}
