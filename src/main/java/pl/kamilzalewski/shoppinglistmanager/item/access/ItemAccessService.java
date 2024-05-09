package pl.kamilzalewski.shoppinglistmanager.item.access;

import jakarta.servlet.http.HttpServletRequest;

public interface ItemAccessService {

    boolean canPerformAllActions(HttpServletRequest request, Long listId);
    boolean canPerformLimitedActions(HttpServletRequest request, Long listId);
}
