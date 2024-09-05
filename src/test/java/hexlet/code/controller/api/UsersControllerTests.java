package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UsersRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTests {
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Faker faker;

    @BeforeEach
    public void generateUser() {
        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPassword), () -> faker.internet().password(3, 10))
                .create();
        usersRepository.save(user);
    }

    @AfterEach
    public void cleanup() {
        if (user != null && user.getId() != null && usersRepository.existsById(user.getId())) {
            usersRepository.delete(user);
        }
    }

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/welcome"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring");
    }

    @Test
    public void testGetUser() throws Exception {
        var request = get("/api/users/" + user.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                e -> e.node("email").isEqualTo(user.getEmail())
        );
    }

    @Test
    public void testGetList() throws Exception {
        var request = get("/api/users");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testCreate() throws Exception {
        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var createdUser = usersRepository.findById(user.getId()).get();
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testUpdate() throws Exception {
        var listUpdate = new HashMap<>();
        listUpdate.put("firstName", "Kate");
        listUpdate.put("lastName", "Morozova");

        var request = put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listUpdate));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        var updatedUser = usersRepository.findById(user.getId()).get();
        assertThat(updatedUser.getFirstName()).isEqualTo("Kate");
        assertThat(updatedUser.getLastName()).isEqualTo("Morozova");
    }

    @Test
    public void testDeleteUser() throws Exception {
        var request = delete("/api/users/" + user.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(usersRepository.findById(user.getId())).isEmpty();
    }
}
