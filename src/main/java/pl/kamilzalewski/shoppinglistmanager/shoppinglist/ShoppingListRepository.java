package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kamilzalewski.shoppinglistmanager.user.User;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer> {

    @Query("Select sl from ShoppingList sl where sl.createdBy = ?1")
    Page<ShoppingList> findListsCreatedByUser(User user, Pageable pageable);

    @Query("Select sl from ShoppingList sl " +
            "join Share sh on sl.id = sh.shoppingList.id " +
            "where sh.userWithShare = ?1")
    Page<ShoppingList> findListsSharedToUser(User user, Pageable pageable);
}
