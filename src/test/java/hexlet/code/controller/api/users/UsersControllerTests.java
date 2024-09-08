package hexlet.code.controller.api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UsersRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsersControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Faker faker;

    @Autowired
    private ModelGenerator modelGenerator;

    private User user;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        user = Instancio.of(modelGenerator.getUserModel())
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
    public void testGetUser() throws Exception {
        var request = get("/api/users/" + user.getId())
                .with(jwt().jwt(builder -> builder.subject(user.getEmail())));
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
        var request = get("/api/users").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "hexlet@example.com")
    public void testCreate() throws Exception {
        var request = post("/api/users")
                .with(token)
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
                .with(jwt().jwt(builder -> builder.subject(user.getEmail())))
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
        var request = delete("/api/users/" + user.getId())
                .with(jwt().jwt(builder -> builder.subject(user.getEmail())));
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(usersRepository.findById(user.getId())).isEmpty();
    }

/*    @Test
    @WithMockUser(roles = "ADMIN", username = "hexlet@example.com")
    public void testGetUser() throws Exception {
        var request = get("/api/users/" + user.getId())
                .with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                e -> e.node("email").isEqualTo(user.getEmail())
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetList() throws Exception {
        var request = get("/api/users").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreate() throws Exception {
        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var createdUser = usersRepository.findById(user.getId()).get();
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdate() throws Exception {
        var listUpdate = new HashMap<>();
        listUpdate.put("firstName", "Kate");
        listUpdate.put("lastName", "Morozova");

        var request = put("/api/users/" + user.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listUpdate));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        var updatedUser = usersRepository.findById(user.getId()).get();
        assertThat(updatedUser.getFirstName()).isEqualTo("Kate");
        assertThat(updatedUser.getLastName()).isEqualTo("Morozova");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser() throws Exception {
        var request = delete("/api/users/" + user.getId())
                .with(jwt().jwt(builder -> builder.subject("hexlet@example.com")));
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(usersRepository.findById(user.getId())).isEmpty();
    }*/
}

