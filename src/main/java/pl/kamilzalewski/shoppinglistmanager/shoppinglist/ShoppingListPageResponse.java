package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import java.util.List;

public record ShoppingListPageResponse(List<ShoppingListDto> lists,
                                       Integer pageNumber,
                                       Integer pageSize,
                                       long totalElements,
                                       int totalPages,
                                       boolean isLast) {
}
