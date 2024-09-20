package hexlet.code.controller.api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskStatusControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    private TaskStatus taskStatusModel;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        taskStatusModel = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(taskStatusModel);

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    @Test
    public void getListTaskStatuses() throws Exception {
        var request = get("/api/task_statuses").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void getTaskStatus() throws Exception {
        var request = get("/api/task_statuses/" + taskStatusModel.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).isNotNull();
        assertThatJson(body).and(v -> v.node("slug").isEqualTo(taskStatusModel.getSlug()));
    }

    @Test
    public void createTaskStatus() throws Exception {
        var newTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();

        var request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTaskStatus));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var createdTaskStatus = taskStatusRepository.findBySlug(newTaskStatus.getSlug()).get();
        assertThat(createdTaskStatus).isNotNull();
        assertThat(createdTaskStatus.getSlug()).isEqualTo(newTaskStatus.getSlug());
    }

    @Test
    public void updateTaskStatus() throws Exception {
        var listUpdate = new HashMap<String, String>();
        listUpdate.put("name", "newName");

        var request = put("/api/task_statuses/" + taskStatusModel.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listUpdate));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        var updateTaskStatus = taskStatusRepository.findById(taskStatusModel.getId()).get();
        assertThat(updateTaskStatus).isNotNull();
        assertThat(updateTaskStatus.getName()).isEqualTo("newName");

    }

    @Test
    public void deleteTaskStatus() throws Exception {
        var request = delete("/api/task_statuses/" + taskStatusModel.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskStatusRepository.findById(taskStatusModel.getId())).isEmpty();
    }
}
