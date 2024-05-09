package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Integer id;

    private String name;

    @ManyToOne
    private User createdBy;

    private LocalDateTime created;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Share> shares;
}
