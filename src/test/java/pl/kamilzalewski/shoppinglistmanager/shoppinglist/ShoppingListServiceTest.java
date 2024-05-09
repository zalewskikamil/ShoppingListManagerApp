package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import pl.kamilzalewski.shoppinglistmanager.jwt.TokenValidityException;
import pl.kamilzalewski.shoppinglistmanager.share.Share;
import pl.kamilzalewski.shoppinglistmanager.share.ShareDto;
import pl.kamilzalewski.shoppinglistmanager.share.ShareDtoMapper;
import pl.kamilzalewski.shoppinglistmanager.share.ShareService;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShoppingListHelper shoppingListHelper;
    @Mock
    private ShareService shareService;

    @InjectMocks
    private ShoppingListService underTest;

    private List<User> users;
    private List<ShoppingList> lists;
    private Share share;
    private final String authorizationHeader = "Bearer someToken";
    private final int pageNumber = 0;
    private final int pageSize = 3;
    private final String sortBy = "id";
    private final String direction = "desc";
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .id(1)
                .username("test1@test.pl")
                .build();
        User user2 = User.builder()
                .id(2)
                .username("test2@test.pl")
                .build();
        this.users = Arrays.asList(user1, user2);

        ShoppingList shoppingList1 = ShoppingList.builder()
                .id(1)
                .name("Lidl")
                .createdBy(user1)
                .created(LocalDateTime.parse("2024-05-30T12:00:00"))
                .build();
        ShoppingList shoppingList2 = ShoppingList.builder()
                .id(2)
                .name("Biedronka")
                .createdBy(user1)
                .created(LocalDateTime.parse("2024-06-01T12:00:00"))
                .build();
        this.lists = Arrays.asList(shoppingList1, shoppingList2);

        this.share = Share.builder()
                .id(1)
                .shoppingList(lists.getFirst())
                .userWithShare(users.get(1))
                .build();

        this.pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());

    }

    @Test
    void shouldReturn2ListsCreatedByUser() {
        User user = users.getFirst();
        Page<ShoppingList> page = new PageImpl<>(lists, pageable, lists.size());

        when(shoppingListHelper.findUserFromAuthorizationHeader(anyString())).thenReturn(user);
        when(shoppingListHelper.getPageable(pageNumber, pageSize, sortBy, direction)).thenReturn(pageable);
        when(shoppingListRepository.findListsCreatedByUser(user, pageable)).thenReturn(page);


        ShoppingListPageResponse response = underTest.getListsCreatedByUser
                (authorizationHeader, pageNumber, pageSize, sortBy, direction);

        assertThat(response.pageNumber()).isEqualTo(pageNumber);
        assertThat(response.pageSize()).isEqualTo(pageSize);
        assertThat(response.totalElements()).isEqualTo(lists.size());
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.isLast()).isEqualTo(true);
    }

    @Test
    void shouldReturn1ListSharedToUser() {
        User user = users.get(1);
        Page<ShoppingList> page = new PageImpl<>(List.of(lists.get(1)), pageable, 1);

        when(shoppingListHelper.findUserFromAuthorizationHeader(anyString())).thenReturn(user);
        when(shoppingListHelper.getPageable(pageNumber, pageSize, sortBy, direction)).thenReturn(pageable);
        when(shoppingListRepository.findListsSharedToUser(user, pageable)).thenReturn(page);

        ShoppingListPageResponse response = underTest.getListsSharedToUser
                (authorizationHeader, pageNumber, pageSize, sortBy, direction);

        assertThat(response.totalElements()).isEqualTo(1);
        assertThat(response.lists().getFirst().getId()).isEqualTo(lists.get(1).getId());

    }

    @Test
    void shouldAddList() {
        String listName = "Lidl";
        User user = users.getFirst();
        ShoppingList shoppingList = lists.getFirst();

        when(shoppingListHelper.findUserFromAuthorizationHeader(anyString())).thenReturn(user);
        when(shoppingListRepository.save(any(ShoppingList.class))).thenReturn(shoppingList);

        ShoppingListDto addedList = underTest.addList(authorizationHeader, new ShoppingListRequest(listName));

        assertThat(addedList.getId()).isEqualTo(shoppingList.getId());
        assertThat(addedList.getName()).isEqualTo(shoppingList.getName());
        assertThat(addedList.getCreatedBy().getId()).isEqualTo(shoppingList.getCreatedBy().getId());

        verify(shoppingListHelper).findUserFromAuthorizationHeader(authorizationHeader);
        verify(shoppingListRepository).save(any(ShoppingList.class));
    }

    @Test
    void shouldShareList() {
        ShoppingList list = lists.get(1);
        User user = users.get(1);

        doNothing().when(shoppingListHelper).checkAuthorizationTokenIsLoggedOut(anyString());
        when(shoppingListHelper.findShoppingListFromRequestData(anyInt(), eq(true)))
                .thenReturn(list);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(shareService.addShare(any(Share.class))).thenReturn(ShareDtoMapper.mapToShareDto(share));

        ShareDto shareDto = underTest.shareList(authorizationHeader, list.getId(), user.getUsername());

        assertThat(shareDto.getId()).isEqualTo(share.getId());
        assertThat(shareDto.getUserEmail()).isEqualTo(share.getUserWithShare().getUsername());
    }

    @Test
    void shouldThrowTokenValidityException() {
        doThrow(new TokenValidityException("Access token is logged out"))
                .when(shoppingListHelper).checkAuthorizationTokenIsLoggedOut(anyString());

        assertThrows(TokenValidityException.class,
                () -> underTest.deleteList(authorizationHeader, lists.getFirst().getId()));

        verify(shoppingListHelper).checkAuthorizationTokenIsLoggedOut(anyString());
        verify(shoppingListHelper, never()).findShoppingListFromRequestData(anyInt(), anyBoolean());
        verify(shoppingListRepository, never()).delete(any(ShoppingList.class));
    }

    @Test
    void shouldThrowShoppingListNotFoundException() {
        int nonExistentId = 3;
        doNothing().when(shoppingListHelper).checkAuthorizationTokenIsLoggedOut(anyString());
        doThrow(new ShoppingListNotFoundException("List with id: " + nonExistentId + " not found"))
                .when(shoppingListHelper).findShoppingListFromRequestData(nonExistentId, true);

        assertThrows(ShoppingListNotFoundException.class,
                () -> underTest.deleteList(authorizationHeader, nonExistentId));
    }
}