package pl.kamilzalewski.shoppinglistmanager.shopping_list;

import jakarta.persistence.*;
import lombok.*;
import pl.kamilzalewski.shoppinglistmanager.item.Item;
import pl.kamilzalewski.shoppinglistmanager.share.Share;
import pl.kamilzalewski.shoppinglistmanager.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shopping_list")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User createdBy;

    private LocalDateTime created;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    @Setter
    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Share> shares;
}
