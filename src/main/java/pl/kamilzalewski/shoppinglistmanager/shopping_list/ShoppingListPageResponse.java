package pl.kamilzalewski.shoppinglistmanager.shopping_list;

import java.util.List;

public record ShoppingListPageResponse(List<ShoppingListDto> lists,
                                       Integer pageNumber,
                                       Integer pageSize,
                                       long totalElements,
                                       int totalPages,
                                       boolean isLast) {
}
