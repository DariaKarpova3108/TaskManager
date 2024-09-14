package hexlet.code.service.TaskService;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper mapper;

    public List<TaskDTO> getListTasks() {
        var listModel = repository.findAll();
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
