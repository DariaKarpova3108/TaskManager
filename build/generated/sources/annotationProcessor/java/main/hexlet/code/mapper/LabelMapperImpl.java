package hexlet.code.mapper;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.model.Label;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-20T23:49:59+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class LabelMapperImpl extends LabelMapper {

    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Override
    public Label map(LabelCreateDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        Label label = new Label();

        label.setName( createDTO.getName() );

        return label;
    }

    @Override
    public LabelDTO map(Label model) {
        if ( model == null ) {
            return null;
        }

        LabelDTO labelDTO = new LabelDTO();

        labelDTO.setId( model.getId() );
        labelDTO.setName( model.getName() );
        labelDTO.setCreatedAt( model.getCreatedAt() );

        return labelDTO;
    }

    @Override
    public void update(LabelUpdateDTO updateDTO, Label label) {
        if ( updateDTO == null ) {
            return;
        }

        if ( jsonNullableMapper.isPresent( updateDTO.getName() ) ) {
            label.setName( jsonNullableMapper.unwrap( updateDTO.getName() ) );
        }
    }
}
