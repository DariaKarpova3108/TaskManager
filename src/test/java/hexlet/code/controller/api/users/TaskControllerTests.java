package hexlet.code.controller.api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UsersRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private UsersRepository usersRepository;

    private Task task;
    private TaskStatus taskStatus;
    private User user;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        task = Instancio.of(modelGenerator.getTaskModel()).create();
        taskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        user = Instancio.of(modelGenerator.getUserModel()).create();

        usersRepository.save(user);
        statusRepository.save(taskStatus);

        task.setTaskStatus(taskStatus);
        task.setAssignee(user);

        taskRepository.save(task);
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

//    @AfterEach
//    public void cleanUp() {
//        if (task != null && task.getId() != null && taskRepository.existsById(task.getId())) {
//            taskRepository.delete(task);
//        }
//    }

    @Test
    public void getListTask() throws Exception {
        var request = get("/api/tasks").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void getTask() throws Exception {
        var request = get("/api/tasks/" + task.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThat(body).isNotNull();
        assertThatJson(body).and(v -> v.node("id").isEqualTo(task.getId()));
    }

    @Test
    public void createTask() throws Exception {
        var createDTO = new TaskCreateDTO();
        createDTO.setTitle(task.getName());
        createDTO.setContent(task.getDescription());
        createDTO.setIndex(task.getIndex());
        createDTO.setAssigneeId(user.getId());
        createDTO.setStatus(task.getTaskStatus().getSlug());
        var request = post("/api/tasks").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var createdTask = taskRepository.findById(task.getId()).get();
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getName()).isEqualTo(createDTO.getTitle());
    }

    @Test
    public void updateTask() throws Exception {
        var updateDTO = new TaskUpdateDTO();
        updateDTO.setTitle(JsonNullable.of("newName"));

        var request = put("/api/tasks/" + task.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedTask = taskRepository.findById(task.getId()).get();
        assertThat(updatedTask.getName()).isEqualTo("newName");
    }

    @Test
    public void deleteTask() throws Exception {
        var request = delete("/api/tasks/" + task.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }
}
