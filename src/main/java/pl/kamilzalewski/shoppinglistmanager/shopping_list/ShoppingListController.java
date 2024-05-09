package pl.kamilzalewski.shoppinglistmanager.shopping_list;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.access.ShoppingListAccessService;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.service.ShoppingListModificationService;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.service.ShoppingListReadService;
import pl.kamilzalewski.shoppinglistmanager.sorting_pagination.SortingPaginationConstants;

@RestController
@RequestMapping("/api/list")
@RequiredArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ShoppingListController {

    private final ShoppingListReadService readService;
    private final ShoppingListModificationService modificationService;
    private final ShoppingListAccessService accessService;
    private final HttpServletRequest request;

    @GetMapping("/created")
    public ResponseEntity<ShoppingListPageResponse> getListsCreatedByUser(
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = SortingPaginationConstants.SHOPPING_LIST_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_DIRECTION, required = false) String direction
    ) {
        if (!accessService.canAccessList(request)) {
            throw new AccessDeniedException("Access denied");
        }
        return ResponseEntity.ok(readService.getListsCreatedByUser(pageNumber, pageSize, sortBy, direction));
    }

    @GetMapping("/shared")
    public ResponseEntity<ShoppingListPageResponse> getListsSharedToUser(
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = SortingPaginationConstants.SHOPPING_LIST_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_DIRECTION, required = false) String direction
    ) {
        if (!accessService.canAccessList(request)) {
            throw new AccessDeniedException("Access denied");
        }
        return ResponseEntity.ok(readService.getListsSharedToUser(pageNumber, pageSize, sortBy, direction));
    }

    @PostMapping("")
    public ResponseEntity<ShoppingListDto> addList(@RequestParam String listName) {
        if (!accessService.canAccessList(request)) {
            throw new AccessDeniedException("Access denied");
        }
        return new ResponseEntity<>(modificationService.addList(listName), HttpStatus.CREATED);
    }

    @PatchMapping("/{listId}")
    public ResponseEntity<ShoppingListDto> changeListName(@PathVariable Long listId, @RequestParam String newListName) {
        if (!accessService.canModifyList(request, listId)) {
            throw new AccessDeniedException("Access denied");
        }
        return ResponseEntity.ok(modificationService.changeListName(listId, newListName));
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable Long listId) {
        if (!accessService.canModifyList(request, listId)) {
            throw new AccessDeniedException("Access denied");
        }
        modificationService.deleteList(listId);
        return ResponseEntity.noContent().build();
    }
}
