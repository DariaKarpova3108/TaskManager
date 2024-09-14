package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UsersRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusSlugToTaskStatus")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "usersIdToAssignee")
    public abstract Task map(TaskCreateDTO createDTO);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    public abstract TaskDTO map(Task task);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    public abstract void update(TaskUpdateDTO updateDTO, @MappingTarget Task task);

    @Named("statusSlugToTaskStatus")
    public TaskStatus statusSlugToTaskStatus(String statusSlug) {
        var taskStatus = taskStatusRepository.findBySlug(statusSlug)
                .orElseThrow();
        return taskStatus;
    }

    @Named("usersIdToAssignee")
    public User usersIdToAssignee(Long id) {
        var user = usersRepository.findById(id)
                .orElseThrow();
        return user;
    }
}
