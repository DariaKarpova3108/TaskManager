package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import javax.annotation.processing.Generated;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-23T20:59:16+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl extends TaskMapper {

    @Autowired
    private JsonNullableMapper jsonNullableMapper;
    @Autowired
    private ReferenceMapper referenceMapper;

    @Override
    public Task map(TaskCreateDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        Task task = new Task();

        task.setName( createDTO.getTitle() );
        task.setDescription( createDTO.getContent() );
        task.setTaskStatus( statusSlugToTaskStatus( createDTO.getStatus() ) );
        task.setAssignee( referenceMapper.toEntity( createDTO.getAssigneeId(), User.class ) );
        task.setLabels( labelsIdToModel( createDTO.getLabelsId() ) );
        task.setIndex( createDTO.getIndex() );

        return task;
    }

    @Override
    public TaskDTO map(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setTitle( task.getName() );
        taskDTO.setContent( task.getDescription() );
        taskDTO.setStatus( taskTaskStatusSlug( task ) );
        taskDTO.setAssigneeId( taskAssigneeId( task ) );
        taskDTO.setLabelsId( labelToLabelID( task.getLabels() ) );
        taskDTO.setId( task.getId() );
        taskDTO.setIndex( task.getIndex() );
        taskDTO.setCreatedAt( task.getCreatedAt() );

        return taskDTO;
    }

    @Override
    public void update(TaskUpdateDTO updateDTO, Task task) {
        if ( updateDTO == null ) {
            return;
        }

        if ( jsonNullableMapper.isPresent( updateDTO.getTitle() ) ) {
            task.setName( jsonNullableMapper.unwrap( updateDTO.getTitle() ) );
        }
        if ( jsonNullableMapper.isPresent( updateDTO.getContent() ) ) {
            task.setDescription( jsonNullableMapper.unwrap( updateDTO.getContent() ) );
        }
        if ( task.getLabels() != null ) {
            if ( jsonNullableMapper.isPresent( updateDTO.getLabelsId() ) ) {
                task.getLabels().clear();
                task.getLabels().addAll( labelsIdToModel( jsonNullableMapper.unwrap( updateDTO.getLabelsId() ) ) );
            }
        }
        else {
            if ( jsonNullableMapper.isPresent( updateDTO.getLabelsId() ) ) {
                task.setLabels( labelsIdToModel( jsonNullableMapper.unwrap( updateDTO.getLabelsId() ) ) );
            }
        }
        if ( jsonNullableMapper.isPresent( updateDTO.getAssigneeId() ) ) {
            task.setAssignee( referenceMapper.toEntity( jsonNullableMapper.unwrap( updateDTO.getAssigneeId() ), User.class ) );
        }
        if ( jsonNullableMapper.isPresent( updateDTO.getStatus() ) ) {
            if ( task.getTaskStatus() == null ) {
                task.setTaskStatus( new TaskStatus() );
            }
            stringJsonNullableToTaskStatus( updateDTO.getStatus(), task.getTaskStatus() );
        }
        if ( jsonNullableMapper.isPresent( updateDTO.getIndex() ) ) {
            task.setIndex( jsonNullableMapper.unwrap( updateDTO.getIndex() ) );
        }
    }

    private String taskTaskStatusSlug(Task task) {
        if ( task == null ) {
            return null;
        }
        TaskStatus taskStatus = task.getTaskStatus();
        if ( taskStatus == null ) {
            return null;
        }
        String slug = taskStatus.getSlug();
        if ( slug == null ) {
            return null;
        }
        return slug;
    }

    private Long taskAssigneeId(Task task) {
        if ( task == null ) {
            return null;
        }
        User assignee = task.getAssignee();
        if ( assignee == null ) {
            return null;
        }
        Long id = assignee.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected void stringJsonNullableToTaskStatus(JsonNullable<String> jsonNullable, TaskStatus mappingTarget) {
        if ( jsonNullable == null ) {
            return;
        }
    }
}
