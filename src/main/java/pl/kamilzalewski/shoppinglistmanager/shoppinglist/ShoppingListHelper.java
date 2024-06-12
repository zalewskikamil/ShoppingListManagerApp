package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.kamilzalewski.shoppinglistmanager.item.Item;
import pl.kamilzalewski.shoppinglistmanager.item.ItemNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.jwt.AccessToTheListException;
import pl.kamilzalewski.shoppinglistmanager.jwt.AccessToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.AccessTokenService;
import pl.kamilzalewski.shoppinglistmanager.jwt.TokenValidityException;
import pl.kamilzalewski.shoppinglistmanager.share.Share;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.exceptions.UserNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ShoppingListHelper {

    private final AccessTokenService accessTokenService;
    private final UserRepository userRepository;
    private final ShoppingListRepository shoppingListRepository;



    public void checkAuthorizationTokenIsLoggedOut(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        AccessToken accessToken = accessTokenService.findAccessToken(token);
        if (accessToken.isLoggedOut()) {
            throw new TokenValidityException("Access token is logged out");
        }
    }

    public User findUserFromAuthorizationHeader(String authorizationHeader) {
        checkAuthorizationTokenIsLoggedOut(authorizationHeader);
        String userEmail = getUserEmailFromContext();
        return userRepository.findByUsername(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + userEmail + " not found"));
    }

    public ShoppingList findShoppingListFromRequestData(Integer listId, Integer itemId, boolean hasCreatorAccessOnly) {
        ShoppingList list = findShoppingListFromRequestData(listId, hasCreatorAccessOnly);
        List<Item> items = list.getItems();
        boolean isItemOnTheList = items.stream()
                .anyMatch(item -> Objects.equals(item.getId(), itemId));
        if (!isItemOnTheList) {
            throw new ItemNotFoundException("Item not found in the list with id: " + listId);
        }
        return list;
    }

    public ShoppingList findShoppingListFromRequestData(Integer listId, boolean hasCreatorAccessOnly) {
        ShoppingList list = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException("List with id: " + listId + " not found"));
        String userEmail = getUserEmailFromContext();
        if (hasCreatorAccessOnly) {
            if (!isUserListCreator(userEmail, list)) {
                throw new AccessToTheListException(
                        "The user  " + userEmail + " has no access to the list with id: " + listId);
            }
        } else {
            if (!hasUserAccessToList(userEmail, list)) {
                throw new AccessToTheListException(
                        "The user  " + userEmail + " has no access to the list with id: " + listId);
            }
        }
        return list;
    }

    private boolean isUserListCreator(String userEmail, ShoppingList shoppingList) {
        return shoppingList.getCreatedBy().getUsername().equals(userEmail);
    }

    private boolean hasUserAccessToList(String userEmail, ShoppingList shoppingList) {
        if (isUserListCreator(userEmail, shoppingList)) return true;
        List<Share> shares = shoppingList.getShares();
        return shares.stream()
                .anyMatch(share ->
                        share.getUserWithShare().getUsername().equals(userEmail));
    }

    private String getUserEmailFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public Pageable getPageable(Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
