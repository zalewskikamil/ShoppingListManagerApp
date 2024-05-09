package pl.kamilzalewski.shoppinglistmanager.jwt.access_token;

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
    private Long id;

    private String token;

    @Setter
    private boolean isLoggedOut;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}