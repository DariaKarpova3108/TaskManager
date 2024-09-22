package hexlet.code.util;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelsRepository;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGenerator {
    private Model<User> userModel;
    private Model<TaskStatus> taskStatusModel;
    private Model<Task> taskModel;
    private Model<Label> labelModel;

    @Autowired
    private Faker faker;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelsRepository labelsRepository;

    @PostConstruct
    private void generate() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password(3, 10))
                .ignore(Select.field(User::getLisTasks))
                .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.name().title())
                .supply(Select.field(TaskStatus::getSlug), this::generateUniqueSlug)
                .ignore(Select.field(TaskStatus::getListTasks))
                .toModel();

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getName), () -> faker.name().title())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .supply(Select.field(Task::getIndex), () -> faker.number().numberBetween(1, 100))
                .ignore(Select.field(Task::getTaskStatus))
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getLabels))
                .toModel();

        labelModel = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .supply(Select.field(Label::getName), this::generateUniqueName)
                .ignore(Select.field(Label::getTasks))
                .toModel();
    }

    private String generateUniqueSlug() {
        String slug = faker.internet().slug();
        if (taskStatusRepository.existsBySlug(slug)) {
            return generateUniqueSlug();
        }
        return slug;
    }

    private String generateUniqueName() {
        String nameLabel;
        int i = 0;
        int step = 100;

        do {
            nameLabel = faker.name().title();
            i++;
            if (nameLabel.length() < 3 || labelsRepository.existsByName(nameLabel)) {
                continue;
            }
            return nameLabel;
        } while (i < step);
        throw new IllegalStateException("Failed to generate unique name after " + step + " attempts");
    }
}
