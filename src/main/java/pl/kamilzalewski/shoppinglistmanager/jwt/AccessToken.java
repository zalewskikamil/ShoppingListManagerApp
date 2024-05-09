package pl.kamilzalewski.shoppinglistmanager.jwt;

import jakarta.persistence.*;
import lombok.*;
import pl.kamilzalewski.shoppinglistmanager.user.User;

@Entity
@Table(name = "access_token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    private boolean isLoggedOut;

    @ManyToOne
    private User user;
}
