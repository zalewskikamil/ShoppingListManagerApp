package pl.kamilzalewski.shoppinglistmanager.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingList;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ItemRepository itemRepository;

    public ItemDto getItem(Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id " + itemId));
        return ItemDtoMapper.mapToItemDto(item);
    }

    public ItemPageResponse getItemsByList(ShoppingList shoppingList, Pageable pageable) {
        Page<Item> itemsPage = itemRepository.findByList(shoppingList, pageable);
        List<ItemDto> items = ItemDtoMapper.mapToItemDtos(itemsPage);
        return new ItemPageResponse(items,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                itemsPage.getTotalElements(),
                itemsPage.getTotalPages(),
                itemsPage.isLast());
    }

    public ItemDto addItem(Item item) {
        Item savedItem = itemRepository.save(item);
        return ItemDtoMapper.mapToItemDto(savedItem);
    }

    public ItemDto updateItem(Item item) {
        Item savedItem = itemRepository.save(item);
        return ItemDtoMapper.mapToItemDto(savedItem);
    }

    public ItemDto markItemAsBought(Integer itemId) {
        itemRepository.markAsBought(itemId);
        entityManager.flush();
        entityManager.clear();
        return getItem(itemId);
    }

    public void deleteItem(Integer itemId) {
        itemRepository.deleteById(itemId);
    }
}
