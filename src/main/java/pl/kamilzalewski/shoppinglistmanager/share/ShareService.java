package pl.kamilzalewski.shoppinglistmanager.share;

import org.springframework.data.domain.Pageable;

public interface ShareService {

    SharePageResponse getSharesByList(Long shoppingListId, Pageable pageable);
    ShareDto addShare(Long listId, String userEmail);
    void deleteShare(Long id);
}
