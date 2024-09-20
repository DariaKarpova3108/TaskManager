package hexlet.code.service.LabelService;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelsRepository labelsRepository;

    @Autowired
    private LabelMapper mapper;

    public LabelDTO getLabel(Long id) {
        var label = labelsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found label with id: " + id));
        return mapper.map(label);
    }

    public List<LabelDTO> getListLabels() {
        return labelsRepository.findAll().stream()
                .map(mapper::map)
                .toList();
    }

    public LabelDTO createLabel(LabelCreateDTO createDTO) {
        var model = mapper.map(createDTO);
        labelsRepository.save(model);
        return mapper.map(model);
    }

    public LabelDTO updateLabel(LabelUpdateDTO updateDTO, Long id) {
        var label = labelsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found label with id: " + id));
        mapper.update(updateDTO, label);
        labelsRepository.save(label);
        return mapper.map(label);
    }

    public void deleteLabel(Long id) {
        labelsRepository.deleteById(id);
    }
}
