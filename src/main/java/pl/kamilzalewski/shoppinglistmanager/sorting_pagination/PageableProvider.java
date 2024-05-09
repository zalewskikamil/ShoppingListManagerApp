package pl.kamilzalewski.shoppinglistmanager.sorting_pagination;

import org.springframework.data.domain.Pageable;


public interface PageableProvider {

    Pageable createPageable(Integer pageNumber, Integer pageSize, String sortBy, String direction);
}
