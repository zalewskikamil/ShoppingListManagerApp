package pl.kamilzalewski.shoppinglistmanager.share;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingList;
import pl.kamilzalewski.shoppinglistmanager.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final ShareRepository shareRepository;

    public SharePageResponse getAllSharesByList(ShoppingList shoppingList, Pageable pageable) {
        Page<Share> sharesPage = shareRepository.findByList(shoppingList, pageable);
        List<ShareDto> shares = ShareDtoMapper.mapToShareDtos(sharesPage);
        return new SharePageResponse(shares,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sharesPage.getTotalElements(),
                sharesPage.getTotalPages(),
                sharesPage.isLast());
    }

    public ShareDto addShare(Share share) {
        ShoppingList sharedList = share.getShoppingList();
        User sharedUser = share.getUserWithShare();
        if (sharedUser.equals(sharedList.getCreatedBy())) {
            throw new ImpossibleShareException("Could not share list to creator");
        }
        List<Share> shares = shareRepository.findByList(sharedList);
        if (!shares.isEmpty()) {
            boolean hasUserAccessToListAlready = shares.stream()
                    .anyMatch(sh -> sh.getUserWithShare().equals(sharedUser));
            if (hasUserAccessToListAlready) {
                throw new RuntimeException("User has already access to the list");
            }
        }
        Share addedShare = shareRepository.save(share);
        return ShareDtoMapper.mapToShareDto(addedShare);
    }

    public void deleteShare(Integer id) {
        Share share = shareRepository.findById(id)
                .orElseThrow(() -> new ShareNotFoundException("Share with id " + id + " not found"));
        shareRepository.delete(share);
    }
}
