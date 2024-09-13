package hexlet.code.controller.api.taskStatus;

import hexlet.code.dto.task_statuses.TaskStatusCreateDTO;
import hexlet.code.dto.task_statuses.TaskStatusDTO;
import hexlet.code.dto.task_statuses.TaskStatusUpdateDTO;
import hexlet.code.service.TaskStatusService.TaskStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
public class TaskStatusController {
    @Autowired
    private TaskStatusService statusService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskStatusDTO> getListTaskStatus() {
        return statusService.getList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO getTaskStatus(@PathVariable Long id) {
        return statusService.getTaskStatus(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO createTaskStatus(@RequestBody @Valid TaskStatusCreateDTO createDTO) {
        return statusService.create(createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO updateTaskStatus(@RequestBody @Valid TaskStatusUpdateDTO updateDTO, @PathVariable Long id) {
        return statusService.update(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskStatus(@PathVariable Long id) {
        statusService.delete(id);
    }

}
