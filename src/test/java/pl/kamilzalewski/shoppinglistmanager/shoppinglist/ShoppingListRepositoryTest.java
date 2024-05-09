package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

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
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
class ShoppingListRepositoryTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingListRepository underTest;

    @BeforeEach
    public void setUp() {
        flyway.migrate();
    }
    @AfterEach
    void tearDown() {
        flyway.clean();
    }

    @Test
    void shouldReturnPageWith2CreatedListsSortedByNameInAscendingOrder() {
        User user = userRepository.findById(1).orElse(null);
        assertThat(user).isNotNull();
        Pageable pageable = PageRequest.of(0, 2, Sort.by("name").ascending());

        Page<ShoppingList> page = underTest.findListsCreatedByUser(user, pageable);
        List<ShoppingList> lists = page.getContent();

        assertThat(lists.size()).isEqualTo(2);
        assertThat(lists.getFirst().getName()).isEqualTo("Aldi");
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    void shouldReturnPageWith1CreatedList() {
        User user = userRepository.findById(1).orElse(null);
        assertThat(user).isNotNull();
        Pageable pageable = PageRequest.of(1, 2, Sort.by("id").descending());

        Page<ShoppingList> page = underTest.findListsCreatedByUser(user, pageable);
        List<ShoppingList> lists = page.getContent();

        assertThat(lists.size()).isEqualTo(1);
        assertThat(lists.getFirst().getId()).isEqualTo(1);
        assertThat(lists.getFirst().getName()).isEqualTo("Biedronka");
    }

    @Test
    void shouldReturnEmptyCreatedListsPage() {
        User user = userRepository.findById(1).orElse(null);
        assertThat(user).isNotNull();
        Pageable pageable = PageRequest.of(1, 3, Sort.by("id").ascending());

        Page<ShoppingList> page = underTest.findListsCreatedByUser(user, pageable);
        List<ShoppingList> lists = page.getContent();

        assertThat(lists).isEmpty();
    }

    @Test
    void shouldReturnPageWith2SharedLists() {
        User user = userRepository.findById(2).orElse(null);
        assertThat(user).isNotNull();
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());

        Page<ShoppingList> page = underTest.findListsSharedToUser(user, pageable);
        List<ShoppingList> lists = page.getContent();

        assertThat(lists.size()).isEqualTo(2);
        assertThat(lists.getFirst().getId()).isEqualTo(1);
        assertThat(lists.get(1).getName()).isEqualTo("Aldi");
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    void shouldReturnEmptySharedListsPage() {
        User user = userRepository.findById(1).orElse(null);
        assertThat(user).isNotNull();
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());

        Page<ShoppingList> page = underTest.findListsSharedToUser(user, pageable);

        assertThat(page.getTotalElements()).isEqualTo(0);
    }
}