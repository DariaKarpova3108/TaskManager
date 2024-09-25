package hexlet.code.service.task_service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    private  final TaskMapper mapper;

    private final TaskSpecification specification;

    public List<TaskDTO> getListTasksWithParams(TaskParamDTO paramDTO, @RequestParam(defaultValue = "1") int page) {
        var spec = specification.build(paramDTO);
        var listModel = repository.findAll(spec, PageRequest.of(page - 1, 10));
        return listModel.stream()
                .map(mapper::map)
                .toList();
    }

    public TaskDTO getTask(Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + id + " not found"));
        return mapper.map(task);
    }

    public TaskDTO createTask(TaskCreateDTO createDTO) {
        var task = mapper.map(createDTO);
        repository.save(task);
        return mapper.map(task);
    }

    public TaskDTO updateTask(TaskUpdateDTO updateDTO, Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + id + " not found"));
        mapper.update(updateDTO, task);
        repository.save(task);
        return mapper.map(task);
    }

    public void deleteTask(Long id) {
        repository.deleteById(id);
    }
}
