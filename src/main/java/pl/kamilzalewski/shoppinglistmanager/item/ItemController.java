package pl.kamilzalewski.shoppinglistmanager.item;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import pl.kamilzalewski.shoppinglistmanager.item.access.ItemAccessService;
import pl.kamilzalewski.shoppinglistmanager.shopping_list.AccessDeniedException;
import pl.kamilzalewski.shoppinglistmanager.sorting_pagination.SortingPaginationConstants;

@RestController
@RequestMapping("/api/list/{listId}/item")
@RequiredArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ItemController {

    private final ItemAccessService accessService;
    private final ItemReadService readService;
    private final ItemModificationService modificationService;
    private final HttpServletRequest request;

    @GetMapping("")
    public ResponseEntity<ItemPageResponse> getItemsByList(
            @PathVariable Long listId,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_DIRECTION, required = false) String direction
    ) {
        if (!accessService.canPerformLimitedActions(request, listId)) {
            throw new AccessDeniedException("No access to shopping list with ID " + listId);
        }
        return ResponseEntity.ok(readService.getItemsByList(listId, pageNumber, pageSize, sortBy, direction));
    }

    @PostMapping("")
    public ResponseEntity<ItemDto> addItem(@PathVariable Long listId, @Valid @RequestBody ItemRequest itemRequest) {
        if (!accessService.canPerformAllActions(request, listId)) {
            throw new AccessDeniedException("No permission to add item");
        }
        return new ResponseEntity<>(modificationService.addItem(listId, itemRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long listId,
                                              @PathVariable Long itemId ,
                                              @Valid @RequestBody ItemRequest itemRequest) {
        if (!accessService.canPerformAllActions(request, listId)) {
            throw new AccessDeniedException("No permission to update item with ID " + itemId);
        }
        return ResponseEntity.ok(modificationService.updateItem(listId, itemId, itemRequest));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> markItemAsBought(@PathVariable Long listId,
                                                    @PathVariable Long itemId) {
        if (!accessService.canPerformLimitedActions(request, listId)) {
            throw new AccessDeniedException("No access to shopping list with ID " + listId);
        }
        return ResponseEntity.ok(modificationService.markItemAsBought(listId, itemId));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long listId,
                                           @PathVariable Long itemId) {
        if (!accessService.canPerformAllActions(request, listId)) {
            throw new AccessDeniedException("No permission to delete item with ID " + itemId);
        }
        modificationService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
