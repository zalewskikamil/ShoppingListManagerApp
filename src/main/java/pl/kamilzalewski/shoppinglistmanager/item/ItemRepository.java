package pl.kamilzalewski.shoppinglistmanager.item;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingList;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("Select i from Item i where i.shoppingList = ?1")
    Page<Item> findByList(ShoppingList shoppingList, Pageable pageable);

    @Transactional
    @Modifying
    @Query("Update Item i set i.isBought = true where i.id = ?1")
    void markAsBought(Integer itemId);
}
