package pl.kamilzalewski.shoppinglistmanager.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.jwt.access_token.AccessToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.access_token.AccessTokenRepository;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingList;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingListRepository;
import pl.kamilzalewski.shoppinglistmanager.user.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomAuthFilterService {

    private final AccessTokenRepository tokenRepository;
    private final ShoppingListRepository listRepository;
    private final CurrentUserService currentUserService;

    public boolean isAccessTokenValid(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Optional<AccessToken> optionalAccessToken = tokenRepository.findByToken(token);
        return optionalAccessToken.filter(accessToken -> !accessToken.isLoggedOut()).isPresent();
    }

    public boolean isUserListCreator(Long shoppingListId) {
        Optional<ShoppingList> optionalShoppingList = listRepository.findById(shoppingListId);
        if (optionalShoppingList.isEmpty()) {
            return false;
        }
        ShoppingList shoppingList = optionalShoppingList.get();
        String listCreatorName = shoppingList.getCreatedBy().getUsername();
        String currentUsername = currentUserService.getCurrentUsername();
        return listCreatorName.equals(currentUsername);
    }

    public boolean isListSharedToUser(Long shoppingListId) {
        Optional<ShoppingList> optionalShoppingList = listRepository.findById(shoppingListId);
        if (optionalShoppingList.isEmpty()) {
            return false;
        }
        ShoppingList shoppingList = optionalShoppingList.get();
        User currentUser = currentUserService.getCurrentUser();
        return shoppingList.getShares().stream()
                .anyMatch(s -> s.getUserWithShare().equals(currentUser));
    }
}
