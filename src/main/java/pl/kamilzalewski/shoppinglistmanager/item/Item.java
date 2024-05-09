package pl.kamilzalewski.shoppinglistmanager.item;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingList;

@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private double quantity;

    private String unit;

    private String description;

    private boolean isBought;

    @ManyToOne
    private ShoppingList shoppingList;
}
