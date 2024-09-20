package hexlet.code.mapper;

import hexlet.code.dto.task_statuses.TaskStatusCreateDTO;
import hexlet.code.dto.task_statuses.TaskStatusDTO;
import hexlet.code.dto.task_statuses.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class TaskStatusMapper {

    public abstract TaskStatus map(TaskStatusCreateDTO createDTO);

    public abstract TaskStatusDTO map(TaskStatus taskStatus);

    public abstract void update(TaskStatusUpdateDTO updateDTO, @MappingTarget TaskStatus taskStatus);
}
