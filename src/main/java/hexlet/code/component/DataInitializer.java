package hexlet.code.component;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.task_statuses.TaskStatusCreateDTO;
import hexlet.code.model.User;
import hexlet.code.repository.LabelsRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UsersRepository;
import hexlet.code.service.label_service.LabelService;
import hexlet.code.service.task_status_service.TaskStatusService;
import hexlet.code.service.user_service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final CustomUserDetailsService usersService;
    private final UsersRepository usersRepository;
    private final TaskStatusService taskStatusService;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelService labelService;
    private final LabelsRepository labelsRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        generateModels();
    }

    public void generateModels() {
        var admin = new User();
        admin.setEmail("hexlet@example.com");
        admin.setPasswordDigest("qwerty");
        admin.setFirstName("admin");
        usersService.createUser(admin);
        var savedUser = usersRepository.findByEmail("hexlet@example.com").get();

        var statusDraft = new TaskStatusCreateDTO();
        statusDraft.setName("Draft");
        statusDraft.setSlug("draft");
        taskStatusService.create(statusDraft);
        var draft = taskStatusRepository.findBySlug("draft");

        var statusToReview = new TaskStatusCreateDTO();
        statusToReview.setName("To_review");
        statusToReview.setSlug("to_review");
        taskStatusService.create(statusToReview);
        var toReview = taskStatusRepository.findBySlug("to_review");

        var statusToBeFixed = new TaskStatusCreateDTO();
        statusToBeFixed.setName("To_be_fixed");
        statusToBeFixed.setSlug("to_be_fixed");
        taskStatusService.create(statusToBeFixed);
        var toBeFixed = taskStatusRepository.findBySlug("to_be_fixed");

        var statusToPublish = new TaskStatusCreateDTO();
        statusToPublish.setName("To_publish");
        statusToPublish.setSlug("to_publish");
        taskStatusService.create(statusToPublish);
        var toPublish = taskStatusRepository.findBySlug("to_publish");

        var statusPublished = new TaskStatusCreateDTO();
        statusPublished.setName("Published");
        statusPublished.setSlug("published");
        taskStatusService.create(statusPublished);
        var publish = taskStatusRepository.findBySlug("published");

        var labelFeature = new LabelCreateDTO();
        labelFeature.setName("feature");
        labelService.createLabel(labelFeature);
        var feature = labelsRepository.findByName("feature");

        var labelBug = new LabelCreateDTO();
        labelBug.setName("bug");
        labelService.createLabel(labelBug);
        var bug = labelsRepository.findByName("bug");
    }
}
