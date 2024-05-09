package pl.kamilzalewski.shoppinglistmanager.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingList;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService underTest;

    @Test
    void shouldReturnItemWithId1() {
        ShoppingList shoppingList = ShoppingList.builder()
                .id(1)
                .name("Lidl")
                .build();
        Item item = Item.builder()
                .id(1)
                .name("apples")
                .quantity(2.0)
                .unit("kg")
                .description("red")
                .isBought(false)
                .shoppingList(shoppingList)
                .build();

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        ItemDto expected = underTest.getItem(1);

        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(item.getId());
    }

    @Test
    void shouldThrowItemNotFoundException() {
        int idDoNotExistInDB = 5;
        when(itemRepository.findById(5)).thenReturn(Optional.empty());
        ItemNotFoundException expected = assertThrows(ItemNotFoundException.class,
                () -> underTest.getItem(idDoNotExistInDB));

        assertThat(expected.getMessage()).isEqualTo("Item not found with id 5");
        verify(itemRepository, times(1)).findById(5);
    }
}