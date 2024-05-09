package pl.kamilzalewski.shoppinglistmanager.share;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingList;
import pl.kamilzalewski.shoppinglistmanager.user.User;

@Entity
@Table(name = "share")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private ShoppingList shoppingList;

    @ManyToOne
    private User userWithShare;
}
