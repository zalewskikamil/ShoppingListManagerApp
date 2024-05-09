package pl.kamilzalewski.shoppinglistmanager.item;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingList;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingListRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ItemRepository underTest;

    @BeforeEach
    public void setUp() {
        flyway.migrate();
    }

    @AfterEach
    void tearDown() {
        flyway.clean();
    }

    @Test
    void shouldReturn2ItemsSortedByIdInAscendingOrder() {
        ShoppingList list = shoppingListRepository.findById(1).orElse(null);
        assertThat(list).isNotNull();
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        Page<Item> page = underTest.findByList(list, pageable);
        List<Item> items = page.getContent();

        assertThat(items.size()).isEqualTo(2);
        assertThat(items.getFirst().getName()).isEqualTo("paprika");
        assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    void shouldReturnItemMarkedAsBought() {
        underTest.markAsBought(1);
        Item item = underTest.findById(1).get();
        assertThat(item.getId()).isEqualTo(1);
        assertThat(item.isBought()).isEqualTo(true);
    }
}