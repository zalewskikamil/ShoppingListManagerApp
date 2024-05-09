package pl.kamilzalewski.shoppinglistmanager.shopping_list.access;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.auth.CustomAuthFilterService;

@Service
@RequiredArgsConstructor
public class ShoppingListAccessServiceImpl implements ShoppingListAccessService {

    private final CustomAuthFilterService filterService;

    @Override
    public boolean canAccessList(HttpServletRequest request) {
        return filterService.isAccessTokenValid(request);
    }

    @Override
    public boolean canModifyList(HttpServletRequest request, Long listId) {
        if (!filterService.isAccessTokenValid(request)) {
            return false;
        }
        return filterService.isUserListCreator(listId);
    }
}
