package pl.kamilzalewski.shoppinglistmanager.shopping_list.access;

import jakarta.servlet.http.HttpServletRequest;

public interface ShoppingListAccessService {

    boolean canAccessList(HttpServletRequest request);

    boolean canModifyList(HttpServletRequest request, Long listId);
}
