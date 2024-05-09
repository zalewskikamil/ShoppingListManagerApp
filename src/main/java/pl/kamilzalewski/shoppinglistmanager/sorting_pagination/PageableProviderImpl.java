package pl.kamilzalewski.shoppinglistmanager.sorting_pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableProviderImpl implements PageableProvider {

    @Override
    public Pageable createPageable(Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
