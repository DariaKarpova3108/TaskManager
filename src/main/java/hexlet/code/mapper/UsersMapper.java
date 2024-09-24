package hexlet.code.mapper;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UsersMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO createDTO);

    public abstract User map(UserUpdateDTO model);

    public abstract UserDTO map(User user);

    public abstract User map(UserDTO model);

    @Mapping(target = "passwordDigest", source = "password")
    public abstract void update(UserUpdateDTO updateDTO, @MappingTarget User user);

    @BeforeMapping
    public void hashPassword(UserCreateDTO data) {
        var password = data.getPassword();
        data.setPassword(passwordEncoder.encode(password));
    }

    @BeforeMapping
    public void hashPasswordToUpdate(UserUpdateDTO updateDTO, @MappingTarget User user) {
        if (updateDTO.getPassword() != null && updateDTO.getPassword().isPresent()) {
            var password = updateDTO.getPassword().get();
            user.setPasswordDigest(passwordEncoder.encode(password));
        }
    }
}
