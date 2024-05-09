package pl.kamilzalewski.shoppinglistmanager.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingList;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByShoppingList(ShoppingList shoppingList, Pageable pageable);
}
