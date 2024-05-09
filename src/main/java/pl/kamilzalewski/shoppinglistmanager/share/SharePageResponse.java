package pl.kamilzalewski.shoppinglistmanager.share;

import java.util.List;

public record SharePageResponse(List<ShareDto> shares,
                                Integer pageNumber,
                                Integer pageSize,
                                long totalElements,
                                int totalPages,
                                boolean isLast) {
}
