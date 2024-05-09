package pl.kamilzalewski.shoppinglistmanager.share;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingList;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {

    List<Share> findByShoppingList(ShoppingList shoppingList);

    Page<Share> findByShoppingList(ShoppingList shoppingList, Pageable pageable);
}
