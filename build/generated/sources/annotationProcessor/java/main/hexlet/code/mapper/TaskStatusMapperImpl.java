package hexlet.code.mapper;

import hexlet.code.dto.task_statuses.TaskStatusCreateDTO;
import hexlet.code.dto.task_statuses.TaskStatusDTO;
import hexlet.code.dto.task_statuses.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-20T22:01:25+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class TaskStatusMapperImpl extends TaskStatusMapper {

    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Override
    public TaskStatus map(TaskStatusCreateDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        TaskStatus taskStatus = new TaskStatus();

        taskStatus.setName( createDTO.getName() );
        taskStatus.setSlug( createDTO.getSlug() );

        return taskStatus;
    }

    @Override
    public TaskStatusDTO map(TaskStatus taskStatus) {
        if ( taskStatus == null ) {
            return null;
        }

        TaskStatusDTO taskStatusDTO = new TaskStatusDTO();

        taskStatusDTO.setId( taskStatus.getId() );
        taskStatusDTO.setName( taskStatus.getName() );
        taskStatusDTO.setSlug( taskStatus.getSlug() );
        taskStatusDTO.setCreatedAt( taskStatus.getCreatedAt() );

        return taskStatusDTO;
    }

    @Override
    public void update(TaskStatusUpdateDTO updateDTO, TaskStatus taskStatus) {
        if ( updateDTO == null ) {
            return;
        }

        if ( jsonNullableMapper.isPresent( updateDTO.getName() ) ) {
            taskStatus.setName( jsonNullableMapper.unwrap( updateDTO.getName() ) );
        }
        if ( jsonNullableMapper.isPresent( updateDTO.getSlug() ) ) {
            taskStatus.setSlug( jsonNullableMapper.unwrap( updateDTO.getSlug() ) );
        }
    }
}
