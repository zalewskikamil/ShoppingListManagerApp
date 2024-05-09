package pl.kamilzalewski.shoppinglistmanager.item;

import java.util.List;

public record ItemPageResponse (List<ItemDto> items,
                                Integer pageNumber,
                                Integer pageSize,
                                long totalElements,
                                int totalPages,
                                boolean isLast) {
}
