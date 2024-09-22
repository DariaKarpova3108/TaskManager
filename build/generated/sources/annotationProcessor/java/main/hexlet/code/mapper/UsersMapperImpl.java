package hexlet.code.mapper;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.model.User;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-22T16:50:57+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class UsersMapperImpl extends UsersMapper {

    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Override
    public User map(UserCreateDTO createDTO) {
        hashPassword( createDTO );

        if ( createDTO == null ) {
            return null;
        }

        User user = new User();

        user.setPasswordDigest( createDTO.getPassword() );
        user.setFirstName( createDTO.getFirstName() );
        user.setLastName( createDTO.getLastName() );
        user.setEmail( createDTO.getEmail() );

        return user;
    }

    @Override
    public User map(UserUpdateDTO model) {
        if ( model == null ) {
            return null;
        }

        User user = new User();

        if ( jsonNullableMapper.isPresent( model.getFirstName() ) ) {
            user.setFirstName( jsonNullableMapper.unwrap( model.getFirstName() ) );
        }
        if ( jsonNullableMapper.isPresent( model.getLastName() ) ) {
            user.setLastName( jsonNullableMapper.unwrap( model.getLastName() ) );
        }
        if ( jsonNullableMapper.isPresent( model.getEmail() ) ) {
            user.setEmail( jsonNullableMapper.unwrap( model.getEmail() ) );
        }

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
        userDTO.setCreatedAt( user.getCreatedAt() );

        return userDTO;
    }

    @Override
    public User map(UserDTO model) {
        if ( model == null ) {
            return null;
        }

        User user = new User();

        user.setId( model.getId() );
        user.setFirstName( model.getFirstName() );
        user.setLastName( model.getLastName() );
        user.setEmail( model.getEmail() );
        user.setCreatedAt( model.getCreatedAt() );

        return user;
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
    }
}
