package pl.kamilzalewski.shoppinglistmanager.item.access;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.auth.CustomAuthFilterService;

@Service
@RequiredArgsConstructor
public class ItemAccessServiceImpl implements ItemAccessService {

    private final CustomAuthFilterService filterService;

    @Override
    public boolean canPerformAllActions(HttpServletRequest request, Long listId) {
        if (!filterService.isAccessTokenValid(request)) {
            return false;
        }
        return filterService.isUserListCreator(listId);
    }

    @Override
    public boolean canPerformLimitedActions(HttpServletRequest request, Long listId) {
        if (!filterService.isAccessTokenValid(request)) {
            return false;
        }
        if (filterService.isUserListCreator(listId)) {
            return true;
        }
        return filterService.isListSharedToUser(listId);
    }
}
