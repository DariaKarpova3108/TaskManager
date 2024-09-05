package hexlet.code.mapper;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.model.User;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-05T23:43:28+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class UsersMapperImpl extends UsersMapper {

    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Override
    public User map(UserCreateDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( createDTO.getFirstName() );
        user.setLastName( createDTO.getLastName() );
        user.setEmail( createDTO.getEmail() );
        user.setPassword( createDTO.getPassword() );

        return user;
    }

    @Override
    public UserDTO map(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setCreatedAt( user.getCreatedAt() );
        userDTO.setUpdatedAt( user.getUpdatedAt() );

        return userDTO;
    }

    @Override
    public void update(UserUpdateDTO updateDTO, User user) {
        if ( updateDTO == null ) {
            return;
        }

        if ( jsonNullableMapper.isPresent( updateDTO.getFirstName() ) ) {
            user.setFirstName( jsonNullableMapper.unwrap( updateDTO.getFirstName() ) );
        }
        if ( jsonNullableMapper.isPresent( updateDTO.getLastName() ) ) {
            user.setLastName( jsonNullableMapper.unwrap( updateDTO.getLastName() ) );
        }
        if ( jsonNullableMapper.isPresent( updateDTO.getEmail() ) ) {
            user.setEmail( jsonNullableMapper.unwrap( updateDTO.getEmail() ) );
        }
        if ( jsonNullableMapper.isPresent( updateDTO.getPassword() ) ) {
            user.setPassword( jsonNullableMapper.unwrap( updateDTO.getPassword() ) );
        }
    }
}
