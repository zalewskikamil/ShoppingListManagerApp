package pl.kamilzalewski.shoppinglistmanager.share.access;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.auth.CustomAuthFilterService;

@Service
@RequiredArgsConstructor
public class ShareAccessServiceImpl implements ShareAccessService {

    private final CustomAuthFilterService filterService;

    @Override
    public boolean hasAccessToShare(HttpServletRequest request, Long shoppingListId) {
        if (!filterService.isAccessTokenValid(request)) {
            return false;
        }
        return filterService.isUserListCreator(shoppingListId);
    }
}
