package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelsRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

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
    private LabelsRepository labelsRepository;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusSlugToTaskStatus")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "labelsIdToModel")
    public abstract Task map(TaskCreateDTO createDTO);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "labelToLabelID")
    public abstract TaskDTO map(Task task);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "labelsIdToModel")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status")
    public abstract void update(TaskUpdateDTO updateDTO, @MappingTarget Task task);

    @Named("statusSlugToTaskStatus")
    public TaskStatus statusSlugToTaskStatus(String statusSlug) {
        var taskStatus = taskStatusRepository.findBySlug(statusSlug)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with slug: " + statusSlug + " not found"));
        return taskStatus;
    }

    @Named("labelsIdToModel")
    public Set<Label> labelsIdToModel(Set<Long> labelsId) {
        Set<Label> labels = new HashSet<>();
        if (labelsId != null) {
            for (var id : labelsId) {
                var model = labelsRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found label with id: " + id));
                labels.add(model);
            }
        }
        return labels;
    }

    @Named("labelToLabelID")
    public Set<Long> labelToLabelID(Set<Label> labels) {
        Set<Long> labelsID = new HashSet<>();
        if (labels != null) {
            for (var label : labels) {
                labelsID.add(label.getId());
            }
        }
        return labelsID;
    }
}
