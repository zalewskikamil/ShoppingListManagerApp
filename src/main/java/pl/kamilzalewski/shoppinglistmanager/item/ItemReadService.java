package pl.kamilzalewski.shoppinglistmanager.item;

public interface ItemReadService {

    ItemDto getItem(Long itemId);
    ItemPageResponse getItemsByList(Long shoppingListId,
                                    Integer pageNumber, Integer pageSize, String sortBy, String direction);
}
