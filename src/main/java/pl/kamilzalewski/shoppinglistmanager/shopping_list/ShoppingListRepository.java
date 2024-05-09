package pl.kamilzalewski.shoppinglistmanager.shopping_list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kamilzalewski.shoppinglistmanager.user.User;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    @Query("SELECT sl FROM ShoppingList sl WHERE sl.createdBy = ?1")
    Page<ShoppingList> findListsCreatedByUser(User user, Pageable pageable);

    @Query("SELECT sl FROM ShoppingList sl " +
            "JOIN Share sh ON sl.id = sh.shoppingList.id " +
            "WHERE sh.userWithShare = ?1")
    Page<ShoppingList> findListsSharedToUser(User user, Pageable pageable);
}
