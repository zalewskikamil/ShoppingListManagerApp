package pl.kamilzalewski.shoppinglistmanager.shopping_list.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.auth.CurrentUserService;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.*;
import pl.kamilzalewski.shoppinglistmanager.sorting_pagination.PageableProvider;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListServiceImpl implements ShoppingListReadService, ShoppingListModificationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShoppingListServiceImpl.class);

    private final ShoppingListRepository shoppingListRepository;
    private final UserRepository userRepository;
    private final PageableProvider pageableProvider;
    private final CurrentUserService currentUserService;

    @Override
    public ShoppingListPageResponse getListsCreatedByUser(
            Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        User user = currentUserService.getCurrentUser();
        Pageable pageable = pageableProvider.createPageable(pageNumber, pageSize, sortBy, direction);
        Page<ShoppingList> pageListsCreatedByUser = shoppingListRepository.findListsCreatedByUser(user, pageable);
        List<ShoppingListDto> shoppingLists = ShoppingListDtoMapper.mapToListsDtos(pageListsCreatedByUser);
        return new ShoppingListPageResponse(shoppingLists, pageNumber, pageSize,
                pageListsCreatedByUser.getTotalElements(),
                pageListsCreatedByUser.getTotalPages(),
                pageListsCreatedByUser.isLast());
    }

    @Override
    public ShoppingListPageResponse getListsSharedToUser(
            Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        User user = currentUserService.getCurrentUser();
        Pageable pageable = pageableProvider.createPageable(pageNumber, pageSize, sortBy, direction);
        Page<ShoppingList> pageListsSharedToUser = shoppingListRepository.findListsSharedToUser(user, pageable);
        List<ShoppingListDto> shoppingLists = ShoppingListDtoMapper.mapToListsDtos(pageListsSharedToUser);
        return new ShoppingListPageResponse(shoppingLists, pageNumber, pageSize,
                pageListsSharedToUser.getTotalElements(),
                pageListsSharedToUser.getTotalPages(),
                pageListsSharedToUser.isLast());
    }

    @Transactional
    @Override
    public ShoppingListDto addList(String listName) {
        User user = currentUserService.getCurrentUser();
        ShoppingList shoppingList = ShoppingList.builder()
                .name(listName)
                .createdBy(user)
                .created(LocalDateTime.now())
                .build();
        ShoppingList addedList = shoppingListRepository.save(shoppingList);
        LOGGER.info("User with ID {} added list with ID {}", user.getId(), addedList.getId());
        return ShoppingListDtoMapper.mapToListDto(addedList);
    }

    @Transactional
    @Override
    public ShoppingListDto changeListName(Long listId, String newListName) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException("List with id: " + listId + " not found"));
        shoppingList.setName(newListName);
        ShoppingList savedList = shoppingListRepository.save(shoppingList);
        LOGGER.info("The name of the list with ID {} has been changed", savedList.getId());
        return ShoppingListDtoMapper.mapToListDto(savedList);
    }

    @Transactional
    @Override
    public void deleteList(Long listId) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException("List with id " + listId + " not found"));
        User user = shoppingList.getCreatedBy();
        user.getShoppingLists().remove(shoppingList);
        userRepository.save(user);
        LOGGER.info("The list with ID {} has been deleted", listId);
    }
}
