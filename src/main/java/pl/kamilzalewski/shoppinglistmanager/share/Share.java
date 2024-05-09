package pl.kamilzalewski.shoppinglistmanager.share;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingList;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id", referencedColumnName = "id")
    private ShoppingList shoppingList;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userWithShare;
}
