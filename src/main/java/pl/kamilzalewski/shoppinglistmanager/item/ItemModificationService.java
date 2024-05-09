package pl.kamilzalewski.shoppinglistmanager.item;

public interface ItemModificationService {
    ItemDto addItem(Long shoppingListId, ItemRequest itemRequest);
    ItemDto updateItem(Long shoppingListId, Long itemId, ItemRequest itemRequest);
    ItemDto markItemAsBought(Long shoppingListId, Long itemId);
    void deleteItem(Long itemId);
}
