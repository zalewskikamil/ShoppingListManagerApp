package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kamilzalewski.shoppinglistmanager.item.ItemDto;
import pl.kamilzalewski.shoppinglistmanager.item.ItemPageResponse;
import pl.kamilzalewski.shoppinglistmanager.item.ItemRequest;
import pl.kamilzalewski.shoppinglistmanager.share.ShareDto;
import pl.kamilzalewski.shoppinglistmanager.share.SharePageResponse;

@RestController
@RequestMapping("/api/list")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @GetMapping("/created")
    public ResponseEntity<ShoppingListPageResponse> getListsCreatedByUserHandler(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = SortingPaginationConstants.SHOPPING_LIST_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_DIRECTION, required = false) String direction) {

        return ResponseEntity.ok(shoppingListService.getListsCreatedByUser(authorizationHeader,
                pageNumber, pageSize, sortBy, direction));
    }

    @GetMapping("/shared")
    public ResponseEntity<ShoppingListPageResponse> getListSharedToUserHandler(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = SortingPaginationConstants.SHOPPING_LIST_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_DIRECTION, required = false) String direction) {

        return ResponseEntity.ok(shoppingListService.getListsSharedToUser(authorizationHeader,
                pageNumber, pageSize, sortBy, direction));
    }

    @PostMapping
    public ResponseEntity<ShoppingListDto> addListHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                          @Valid @RequestBody ShoppingListRequest shoppingListRequest) {
        return new ResponseEntity<>
                (shoppingListService.addList(authorizationHeader, shoppingListRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{listId}")
    public ResponseEntity<ShoppingListDto> changeNameHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                             @PathVariable Integer listId,
                                                             @Valid @RequestBody ShoppingListRequest shoppingListRequest) {
        return ResponseEntity.ok(shoppingListService.changeName(authorizationHeader, listId, shoppingListRequest));
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteListHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                  @PathVariable Integer listId) {
        shoppingListService.deleteList(authorizationHeader, listId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{listId}/items")
    public ResponseEntity<ItemPageResponse> getListsItemsHandler(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer listId,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_DIRECTION, required = false) String direction) {

        return ResponseEntity.ok(shoppingListService
                .getListsItems(authorizationHeader, listId, pageNumber, pageSize, sortBy, direction));
    }

    @PostMapping("/{listId}/item")
    public ResponseEntity<ItemDto> addItemsHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                   @PathVariable Integer listId,
                                                   @Valid @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<>
                (shoppingListService.addItem(authorizationHeader, listId, itemRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{listId}/item/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader("Authorization") String authorizationHeader,
                                              @PathVariable Integer listId,
                                              @PathVariable Integer itemId ,
                                              @Valid @RequestBody ItemRequest itemRequest) {
        return ResponseEntity.ok(shoppingListService.updateItem(authorizationHeader, listId, itemId, itemRequest));
    }

    @PatchMapping("/{listId}/item/{itemId}")
    public ResponseEntity<ItemDto> markItemAsBoughtHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                           @PathVariable Integer listId,
                                                           @PathVariable Integer itemId) {
        return ResponseEntity.ok(shoppingListService.markItemAsBought(authorizationHeader, listId, itemId));
    }

    @DeleteMapping("/{listId}/item/{itemId}")
    public ResponseEntity<Void> deleteItemHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                  @PathVariable Integer listId,
                                                  @PathVariable Integer itemId) {
        shoppingListService.deleteItem(authorizationHeader, listId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{listId}/shares")
    public ResponseEntity<SharePageResponse> getSharesHandler(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer listId,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = SortingPaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = SortingPaginationConstants.SORT_DIRECTION, required = false) String direction) {

        return ResponseEntity.ok
                (shoppingListService.getShares(authorizationHeader, listId, pageNumber, pageSize, sortBy, direction));
    }

    @PostMapping("/{listId}/share/{userEmail}")
    public ResponseEntity<ShareDto> shareListHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                     @PathVariable Integer listId,
                                                     @PathVariable String userEmail) {
        return new ResponseEntity<>
                (shoppingListService.shareList(authorizationHeader, listId, userEmail), HttpStatus.CREATED);
    }

    @DeleteMapping("/{listId}/share/{shareId}")
    public ResponseEntity<Void> deleteShareHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                   @PathVariable Integer listId,
                                                   @PathVariable Integer shareId) {
        shoppingListService.deleteShare(authorizationHeader, listId, shareId);
        return ResponseEntity.noContent().build();
    }
}
