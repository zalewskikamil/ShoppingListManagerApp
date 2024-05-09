package pl.kamilzalewski.shoppinglistmanager.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingList;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingListNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingListRepository;
import pl.kamilzalewski.shoppinglistmanager.sorting_pagination.PageableProvider;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemReadService, ItemModificationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final PageableProvider pageableProvider;

    @Override
    public ItemDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID " + itemId + " not found"));
        return ItemDtoMapper.mapToItemDto(item);
    }

    @Override
    public ItemPageResponse getItemsByList(Long shoppingListId,
                                           Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFoundException("List with id " + shoppingListId + " not found"));
        Pageable pageable = pageableProvider.createPageable(pageNumber, pageSize, sortBy, direction);
        Page<Item> pageItemsByList = itemRepository.findByShoppingList(shoppingList, pageable);
        List<ItemDto> itemsByList = ItemDtoMapper.mapToItemDtos(pageItemsByList);
        return new ItemPageResponse(
                itemsByList,
                pageNumber,
                pageSize,
                pageItemsByList.getTotalElements(),
                pageItemsByList.getTotalPages(),
                pageItemsByList.isLast());
    }

    @Transactional
    @Override
    public ItemDto addItem(Long shoppingListId, ItemRequest itemRequest) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFoundException("List with id " + shoppingListId + " not found"));
        Item addedItem = itemRepository.save(Item.builder()
                .name(itemRequest.name())
                .quantity(itemRequest.quantity())
                .unit(itemRequest.unit())
                .description(itemRequest.description())
                .isBought(itemRequest.isBought())
                .shoppingList(shoppingList)
                .build());
        LOGGER.info("Added item with ID {} to the list with ID {}", addedItem.getId(), shoppingListId);
        return ItemDtoMapper.mapToItemDto(addedItem);
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long shoppingListId, Long itemId, ItemRequest itemRequest) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFoundException("List with id " + shoppingListId + " not found"));
        Optional<Item> optionalItem = shoppingList.getItems().stream()
                .filter(i -> Objects.equals(i.getId(), itemId))
                .findFirst();
        if (optionalItem.isEmpty()) {
            throw new ItemNotFoundException(
                    "Item with ID " + itemId + " does not belong to list with ID " + shoppingListId);
        }
        Item updatedItem = itemRepository.save(Item.builder()
                .id(itemId)
                .name(itemRequest.name())
                .quantity(itemRequest.quantity())
                .unit(itemRequest.unit())
                .description(itemRequest.description())
                .isBought(itemRequest.isBought())
                .shoppingList(shoppingList)
                .build());
        LOGGER.info("Updated item with ID {}", itemId);
        return ItemDtoMapper.mapToItemDto(updatedItem);
    }

    @Transactional
    @Override
    public ItemDto markItemAsBought(Long shoppingListId, Long itemId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFoundException("List with id " + shoppingListId + " not found"));
        Optional<Item> optionalItem = shoppingList.getItems().stream()
                .filter(i -> Objects.equals(i.getId(), itemId))
                .findFirst();
        if (optionalItem.isEmpty()) {
            throw new ItemNotFoundException(
                    "Item with ID " + itemId + " does not belong to list with ID " + shoppingListId);
        }
        Item item = optionalItem.get();
        item.setBought(true);
        Item savedItem = itemRepository.save(item);
        LOGGER.info("Item with ID {} marked as bought", itemId);
        return ItemDtoMapper.mapToItemDto(savedItem);
    }

    @Transactional
    @Override
    public void deleteItem(Long itemId) {
        Item itemToDelete = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID " + itemId + " not found"));
        ShoppingList shoppingList = itemToDelete.getShoppingList();
        shoppingList.getItems().remove(itemToDelete);
        shoppingListRepository.save(shoppingList);
        LOGGER.info("Item with ID {} has been deleted", itemId);
    }
}
