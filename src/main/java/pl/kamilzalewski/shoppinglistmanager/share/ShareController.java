package pl.kamilzalewski.shoppinglistmanager.share;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import pl.kamilzalewski.shoppinglistmanager.share.access.ShareAccessService;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.AccessDeniedException;
import pl.kamilzalewski.shoppinglistmanager.sorting_pagination.PageableProvider;
import pl.kamilzalewski.shoppinglistmanager.sorting_pagination.SortingPaginationConstants;

@RestController
@RequestMapping("/api/list/{listId}/share")
@RequiredArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ShareController {

    private final ShareAccessService shareAccessService;
    private final ShareService shareService;
    private final HttpServletRequest request;
    private final PageableProvider pageableProvider;

    @GetMapping("")
    public ResponseEntity<SharePageResponse> getSharesByList(
            @PathVariable Long listId,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_DIRECTION, required = false) String direction
            ) {
        if (!shareAccessService.hasAccessToShare(request, listId)) {
            throw new AccessDeniedException("Access denied");
        }
        Pageable pageable = pageableProvider.createPageable(pageNumber, pageSize, sortBy, direction);
        return ResponseEntity.ok(shareService.getSharesByList(listId, pageable));
    }

    @PostMapping("/{userEmail}")
    public ResponseEntity<ShareDto> shareList(@PathVariable Long listId,
                                              @PathVariable String userEmail) {
        if (!shareAccessService.hasAccessToShare(request, listId)) {
            throw new AccessDeniedException("Access denied");
        }
        return new ResponseEntity<>(shareService.addShare(listId, userEmail), HttpStatus.CREATED);
    }

    @DeleteMapping("/{shareId}")
    public ResponseEntity<Void> deleteShare(@PathVariable Long listId,
                                            @PathVariable Long shareId) {
        if (!shareAccessService.hasAccessToShare(request, listId)) {
            throw new AccessDeniedException("Access denied");
        }
        shareService.deleteShare(shareId);
        return ResponseEntity.noContent().build();
    }
}
