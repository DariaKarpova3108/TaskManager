package hexlet.code.mapper;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.*;


@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UsersMapper {

//    @Mapping(target = "", source = "")
    public abstract User map(UserCreateDTO createDTO);
  //  @Mapping(target = "", source = "")
    public abstract UserDTO map(User user);
  //  @Mapping(target = "", source = "")
    public abstract void update(UserUpdateDTO updateDTO, @MappingTarget User user);
}
