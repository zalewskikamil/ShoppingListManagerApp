package pl.kamilzalewski.shoppinglistmanager.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kamilzalewski.shoppinglistmanager.jwt.AccessToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.RefreshToken;
import pl.kamilzalewski.shoppinglistmanager.password.ForgotPasswordToken;
import pl.kamilzalewski.shoppinglistmanager.share.Share;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingList;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean isEnabled;

    private boolean isBlocked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccessToken> tokens;

    @OneToOne(mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ForgotPasswordToken forgotPasswordToken;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingList> shoppingLists;

    @OneToMany(mappedBy = "userWithShare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Share> sharesLists;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
