package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.item.*;
import pl.kamilzalewski.shoppinglistmanager.share.Share;
import pl.kamilzalewski.shoppinglistmanager.share.ShareDto;
import pl.kamilzalewski.shoppinglistmanager.share.SharePageResponse;
import pl.kamilzalewski.shoppinglistmanager.share.ShareService;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.exceptions.UserNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShoppingListService.class);

    private final ShoppingListRepository shoppingListRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final ShareService shareService;
    private final ShoppingListHelper shoppingListHelper;
    @PersistenceContext
    private EntityManager entityManager;

    public ShoppingListPageResponse getListsCreatedByUser(String authorizationHeader,
                                                          Integer pageNumber,
                                                          Integer pageSize,
                                                          String sortBy,
                                                          String direction) {
        User user = shoppingListHelper.findUserFromAuthorizationHeader(authorizationHeader);
        Pageable pageable = shoppingListHelper.getPageable(pageNumber, pageSize, sortBy, direction);
        Page<ShoppingList> userListsPage = shoppingListRepository.findListsCreatedByUser(user, pageable);
        List<ShoppingListDto> userLists = ShoppingListDtoMapper.mapToShoppingListDtos(userListsPage);
        return new ShoppingListPageResponse(userLists, pageNumber, pageSize,
                userListsPage.getTotalElements(),
                userListsPage.getTotalPages(),
                userListsPage.isLast());
    }



    public ShoppingListPageResponse getListsSharedToUser(String authorizationHeader,
                                                      Integer pageNumber,
                                                      Integer pageSize,
                                                      String sortBy,
                                                      String direction) {
        User user = shoppingListHelper.findUserFromAuthorizationHeader(authorizationHeader);
        Pageable pageable = shoppingListHelper.getPageable(pageNumber, pageSize, sortBy, direction);
        Page<ShoppingList> sharedListsPage = shoppingListRepository.findListsSharedToUser(user, pageable);
        List<ShoppingListDto> sharedLists = ShoppingListDtoMapper.mapToShoppingListDtos(sharedListsPage);
        return new ShoppingListPageResponse(sharedLists, pageNumber, pageSize,
                sharedListsPage.getTotalElements(),
                sharedListsPage.getTotalPages(),
                sharedListsPage.isLast());
    }

    @Transactional
    public ShoppingListDto addList(String authorizationHeader, ShoppingListRequest shoppingListRequest) {
        User user = shoppingListHelper.findUserFromAuthorizationHeader(authorizationHeader);
        ShoppingList shoppingList = ShoppingList.builder()
                .name(shoppingListRequest.name())
                .createdBy(user)
                .created(LocalDateTime.now())
                .build();
        ShoppingList addedList = shoppingListRepository.save(shoppingList);
        LOGGER.info("User with ID {} added list with ID {}", user.getId(), addedList.getId());
        return ShoppingListDtoMapper.mapToShoppingListDto(addedList);
    }

    @Transactional
    public ShoppingListDto changeName(String authorizationHeader,
                                      Integer listId,
                                      ShoppingListRequest shoppingListRequest) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        ShoppingList list = shoppingListHelper.findShoppingListFromRequestData(listId, true);
        ShoppingList savedList = shoppingListRepository.save(ShoppingList.builder()
                .id(list.getId())
                .name(shoppingListRequest.name())
                .createdBy(list.getCreatedBy())
                .created(list.getCreated())
                .items(list.getItems())
                .shares(list.getShares())
                .build()
        );
        LOGGER.info("The name of the list with ID {} has been changed", savedList.getId());
        return ShoppingListDtoMapper.mapToShoppingListDto(savedList);
    }

    @Transactional
    public void deleteList(String authorizationHeader, Integer listId) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        ShoppingList list = shoppingListHelper.findShoppingListFromRequestData(listId, true);
        shoppingListRepository.delete(list);
        LOGGER.info("The list with ID {} has been deleted", listId);
    }

    public ItemPageResponse getListsItems(String authorizationHeader,
                                          Integer listId,
                                          Integer pageNumber,
                                          Integer pageSize,
                                          String sortBy,
                                          String direction) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        ShoppingList list = shoppingListHelper.findShoppingListFromRequestData(listId, false);
        Pageable pageable = shoppingListHelper.getPageable(pageNumber, pageSize, sortBy, direction);
        return itemService.getItemsByList(list, pageable);
    }

    @Transactional
    public ItemDto addItem(String authorizationHeader, Integer listId, ItemRequest itemRequest) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        ShoppingList list = shoppingListHelper.findShoppingListFromRequestData(listId, true);
        ItemDto addedItem = itemService.addItem(Item.builder()
                .name(itemRequest.name())
                .quantity(itemRequest.quantity())
                .unit(itemRequest.unit())
                .description(itemRequest.description())
                .isBought(itemRequest.isBought())
                .shoppingList(list)
                .build());
        LOGGER.info("Added item with ID {} to the list with ID {}", addedItem.getId(), listId);
        return addedItem;
    }

    @Transactional
    public ItemDto updateItem(String authorizationHeader, Integer listId, Integer itemId, ItemRequest itemRequest) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        ShoppingList list = shoppingListHelper.findShoppingListFromRequestData(listId, itemId, true);
        ItemDto updatedItem = itemService.updateItem(Item.builder()
                .id(itemId)
                .name(itemRequest.name())
                .quantity(itemRequest.quantity())
                .unit(itemRequest.unit())
                .description(itemRequest.description())
                .isBought(itemRequest.isBought())
                .shoppingList(list)
                .build());
        LOGGER.info("Updated item with ID {}", itemId);
        return updatedItem;
    }

    @Transactional
    public ItemDto markItemAsBought(String authorizationHeader, Integer listId, Integer itemId) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        shoppingListHelper.findShoppingListFromRequestData(listId, itemId, false);
        ItemDto markedItem = itemService.markItemAsBought(itemId);
        LOGGER.info("Item with ID {} marked as bought", itemId);
        return markedItem;
    }

    @Transactional
    public void deleteItem(String authorizationHeader, Integer listId, Integer itemId) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        shoppingListHelper.findShoppingListFromRequestData(listId, itemId, true);
        entityManager.clear();
        itemService.deleteItem(itemId);
        LOGGER.info("Item with ID {} has been deleted", itemId);
    }

    public SharePageResponse getShares(String authorizationHeader,
                                       Integer listId,
                                       Integer pageNumber,
                                       Integer pageSize,
                                       String sortBy,
                                       String direction) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        ShoppingList list = shoppingListHelper.findShoppingListFromRequestData(listId, true);
        Pageable pageable = shoppingListHelper.getPageable(pageNumber, pageSize, sortBy, direction);
        return shareService.getAllSharesByList(list, pageable);
    }

    @Transactional
    public ShareDto shareList(String authorizationHeader, Integer listId, String userEmail) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        ShoppingList list = shoppingListHelper.findShoppingListFromRequestData(listId, true);
        User user = userRepository.findByUsername(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + userEmail + " not found"));
        ShareDto share = shareService.addShare(Share.builder()
                .shoppingList(list)
                .userWithShare(user)
                .build());
        LOGGER.info("The list with ID {} has been shared with the user with ID {}", listId, user.getId());
        return share;
    }

    @Transactional
    public void deleteShare(String authorizationHeader, Integer listId, Integer shareId) {
        shoppingListHelper.checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        shoppingListHelper.findShoppingListFromRequestData(listId, true);
        entityManager.clear();
        shareService.deleteShare(shareId);
        LOGGER.info("Share with ID {} has been deleted", shareId);
    }
}