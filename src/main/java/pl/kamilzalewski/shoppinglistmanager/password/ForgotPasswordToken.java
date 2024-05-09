package pl.kamilzalewski.shoppinglistmanager.password;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kamilzalewski.shoppinglistmanager.user.User;

@Entity
@Table(name = "forgot_password")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String token;

    @OneToOne
    private User user;
}
