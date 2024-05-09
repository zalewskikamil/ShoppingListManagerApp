package pl.kamilzalewski.shoppinglistmanager.share;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.share.exceptions.ImpossibleShareException;
import pl.kamilzalewski.shoppinglistmanager.share.exceptions.ShareNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingList;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingListNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.ShoppingListRepository;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShareServiceImpl.class);

    private final ShareRepository shareRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final UserRepository userRepository;

    @Override
    public SharePageResponse getSharesByList(Long shoppingListId, Pageable pageable) {
        ShoppingList list = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFoundException("Shopping list with id " + shoppingListId +
                        " not found"));
        Page<Share> sharesPage = shareRepository.findByShoppingList(list, pageable);
        List<ShareDto> sharesDtos = ShareDtoMapper.mapToSharesDtos(sharesPage);
        return new SharePageResponse(
                sharesDtos,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sharesPage.getTotalElements(),
                sharesPage.getTotalPages(),
                sharesPage.isLast());
    }

    @Transactional
    @Override
    public ShareDto addShare(Long listId, String userEmail) {
        ShoppingList list = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException("List with id " + listId + " not found"));
        User user = userRepository.findByUsername(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + userEmail + " not found"));
        if (user.equals(list.getCreatedBy())) {
            throw new ImpossibleShareException("Could not share list to creator");
        }
        List<Share> listShares = shareRepository.findByShoppingList(list);
        if (!listShares.isEmpty()) {
            boolean userHasAccessToListAlready = listShares.stream()
                    .anyMatch(sh -> sh.getUserWithShare().equals(user));
            if (userHasAccessToListAlready) {
                throw new ImpossibleShareException("User has already access to the list");
            }
        }
        Share newShare = Share.builder()
                .shoppingList(list)
                .userWithShare(user)
                .build();
        Share addedShare = shareRepository.save(newShare);
        LOGGER.info("The list with ID {} has been shared with the user with ID {}", listId, user.getId());
        return ShareDtoMapper.mapToShareDto(addedShare);
    }

    @Transactional
    @Override
    public void deleteShare(Long shareId) {
        Share shareToDelete = shareRepository.findById(shareId)
                .orElseThrow(() -> new ShareNotFoundException("Share with id " + shareId + " not found"));
        ShoppingList shoppingList = shareToDelete.getShoppingList();
        shoppingList.getShares().remove(shareToDelete);
        shoppingListRepository.save(shoppingList);
        LOGGER.info("Share with ID {} has been deleted", shareId);
    }
}
