package pl.kamilzalewski.shoppinglistmanager.shoppinglist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kamilzalewski.shoppinglistmanager.item.ItemDto;
import pl.kamilzalewski.shoppinglistmanager.jwt.AccessToken;
import pl.kamilzalewski.shoppinglistmanager.jwt.AccessTokenService;
import pl.kamilzalewski.shoppinglistmanager.user.User;
import pl.kamilzalewski.shoppinglistmanager.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ShoppingListControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private UserRepository userRepository;
    private AccessToken accessToken;
    private ShoppingListRequest shoppingListRequest;

    @BeforeEach
    public void setUp() {
        flyway.migrate();

        User user = userRepository.findByUsername("user1@gmail.com").get();
        this.accessToken = accessTokenService.generateAccessToken(user);

        this.shoppingListRequest = new ShoppingListRequest("Kaufland");
    }

    @AfterEach
    void tearDown() {
        flyway.clean();
    }

    @Test
    void shouldReturn3Lists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list/created")
                        .header("Authorization", "Bearer " + accessToken.getToken()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lists.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lists[0].name").value("Biedronka"));
    }

    @Test
    void shouldReturn1Lists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list/created?pageSize=1&sortBy=name&direction=desc")
                        .header("Authorization", "Bearer " + accessToken.getToken()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lists.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lists[0].name").value("Lidl"));
    }

    @Test
    void shouldReturnStatus401ForUsingLoggedOutToken() throws Exception {
        accessTokenService.revokeToken(accessToken);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list/created")
                        .header("Authorization", "Bearer " + accessToken.getToken()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnAddedList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/list")
                        .header("Authorization", "Bearer " + accessToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoppingListRequest)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        ShoppingListDto addedShoppingList = objectMapper.readValue
                (mvcResult.getResponse().getContentAsString(), ShoppingListDto.class);
        assertThat(addedShoppingList.getId()).isEqualTo(5);
    }

    @Test
    void shouldReturnListWithChangedName() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/list/1")
                        .header("Authorization", "Bearer " + accessToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoppingListRequest)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        ShoppingListDto addedShoppingList = objectMapper.readValue
                (mvcResult.getResponse().getContentAsString(), ShoppingListDto.class);
        assertThat(addedShoppingList.getName()).isEqualTo(shoppingListRequest.name());
    }

    @Test
    void shouldReturnStatus204ForCorrectDeleteListRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/list/1")
                        .header("Authorization", "Bearer " + accessToken.getToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatus403ForDeleteListWithoutAccessRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/list/4")
                        .header("Authorization", "Bearer " + accessToken.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldMarkItemAsBought() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/list/1/item/3")
                        .header("Authorization", "Bearer " + accessToken.getToken()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        ItemDto itemDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ItemDto.class);
        assertThat(itemDto.getId()).isEqualTo(3);
        assertThat(itemDto.isBought()).isEqualTo(true);
    }

    @Test
    void shouldReturnStatus204ForCorrectDeleteShareRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/list/1/share/1")
                .header("Authorization", "Bearer " + accessToken.getToken()))
                .andExpect(status().isNoContent());
    }
}