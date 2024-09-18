package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelsRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.UserService.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final CustomUserDetailsService usersService;
    private final TaskStatusRepository taskStatusRepository;
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

        var statusDraft = new TaskStatus();
        statusDraft.setName("Draft");
        statusDraft.setSlug("draft");
        taskStatusRepository.save(statusDraft);

        var statusToReview = new TaskStatus();
        statusToReview.setName("To_review");
        statusToReview.setSlug("to_review");
        taskStatusRepository.save(statusToReview);

        var statusToBeFixed = new TaskStatus();
        statusToBeFixed.setName("To_be_fixed");
        statusToBeFixed.setSlug("to_be_fixed");
        taskStatusRepository.save(statusToBeFixed);

        var statusToPublish = new TaskStatus();
        statusToPublish.setName("To_publish");
        statusToPublish.setSlug("to_publish");
        taskStatusRepository.save(statusToPublish);

        var statusPublished = new TaskStatus();
        statusPublished.setName("Published");
        statusPublished.setSlug("published");
        taskStatusRepository.save(statusPublished);

        var labelFeature = new Label();
        labelFeature.setName("feature");
        labelsRepository.save(labelFeature);

        var labelBug = new Label();
        labelBug.setName("bug");
        labelsRepository.save(labelBug);
    }
}
