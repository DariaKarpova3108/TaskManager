package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamDTO taskParamDTO) {
        return withTitleCont(taskParamDTO.getTitleCont())
                .and(withAssignee(taskParamDTO.getAssigneeId()))
                .and(withStatus(taskParamDTO.getStatus()))
                .and(withLabelId(taskParamDTO.getLabelId()));
    }

    private Specification<Task> withTitleCont(String titleCont) {
        return ((root, query, criteriaBuilder) -> titleCont == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                "%" + titleCont.toLowerCase() + "%"));
    }

    private Specification<Task> withAssignee(Long assigneeId) {
        return ((root, query, criteriaBuilder) -> assigneeId == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId));
    }

    private Specification<Task> withStatus(String status) {
        return ((root, query, criteriaBuilder) -> status == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(criteriaBuilder.lower(root.get("taskStatus")
                .get("slug")), status.toLowerCase()));
    }

    private Specification<Task> withLabelId(Long labelId) {
        return ((root, query, criteriaBuilder) -> labelId == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("labels").get("id"), labelId));
    }
}
