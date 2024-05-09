package pl.kamilzalewski.shoppinglistmanager.shopping_list.service;

import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingListDto;

public interface ShoppingListModificationService {

    ShoppingListDto addList(String listName);
    ShoppingListDto changeListName(Long listId, String newListName);
    void deleteList(Long listId);
}
