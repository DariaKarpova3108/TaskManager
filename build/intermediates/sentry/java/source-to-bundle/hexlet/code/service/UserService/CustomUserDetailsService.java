package hexlet.code.service.UserService;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UsersMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsManager {
    @Autowired
    private UsersMapper mapper;

    //совместить два сервиса для юзеров в один сюда
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email: " + email + " NOT FOUND"));
        return user;
    }

    @Override
    public void createUser(UserDetails userData) {
        var user = new User();
        user.setEmail(userData.getUsername());
        var hashedPassword = passwordEncoder.encode(userData.getPassword()); //тут логика с хэшированным паролем
        user.setPasswordDigest(hashedPassword);
        usersRepository.save(user);
    }


    public List<UserDTO> getAll() {
        var list = usersRepository.findAll();
        var mapList = list.stream()
                .map(mapper::map)
                .toList();
        return mapList;
    }

    public UserDTO getUser(Long id) {
        var user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found user with id: " + id));
        return mapper.map(user);
    }

    public UserDTO create(UserCreateDTO createDTO) {
        var user = mapper.map(createDTO);
        usersRepository.save(user);
        return mapper.map(user);
    }

    public UserDTO update(UserUpdateDTO updateDTO, Long id) {
        var user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found user with id: " + id));
        mapper.update(updateDTO, user);
        usersRepository.save(user);
        return mapper.map(user);
    }

    public void delete(Long id) {
        usersRepository.deleteById(id);
    }


    @Override
    public void updateUser(UserDetails userdata) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    @Override
    public boolean userExists(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }
}
