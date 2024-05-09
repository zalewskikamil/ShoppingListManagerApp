package pl.kamilzalewski.shoppinglistmanager.share.access;

import jakarta.servlet.http.HttpServletRequest;

public interface ShareAccessService {
    boolean hasAccessToShare(HttpServletRequest request, Long shoppingListId);
}
