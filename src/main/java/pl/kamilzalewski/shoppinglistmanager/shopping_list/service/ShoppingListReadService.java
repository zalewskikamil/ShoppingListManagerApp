package pl.kamilzalewski.shoppinglistmanager.shopping_list.service;

import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingListPageResponse;

public interface ShoppingListReadService {
    ShoppingListPageResponse getListsCreatedByUser(Integer pageNumber, Integer pageSize, String sortBy, String direction);
    ShoppingListPageResponse getListsSharedToUser(Integer pageNumber, Integer pageSize, String sortBy, String direction);
}
