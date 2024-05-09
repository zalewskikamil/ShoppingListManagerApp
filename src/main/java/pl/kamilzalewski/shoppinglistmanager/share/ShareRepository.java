package pl.kamilzalewski.shoppinglistmanager.share;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingList;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Integer> {

    @Query("Select s from Share s where s.shoppingList = ?1")
    List<Share> findByList(ShoppingList shoppingList);

    @Query("Select s from Share s where s.shoppingList = ?1")
    Page<Share> findByList(ShoppingList shoppingList, Pageable pageable);
}
