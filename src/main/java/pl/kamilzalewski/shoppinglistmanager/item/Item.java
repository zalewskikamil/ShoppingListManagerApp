package pl.kamilzalewski.shoppinglistmanager.item;

import jakarta.persistence.*;
import lombok.*;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingList;

@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double quantity;

    private String unit;

    private String description;

    @Setter
    private boolean isBought;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id", referencedColumnName = "id")
    private ShoppingList shoppingList;
}
