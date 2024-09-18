package hexlet.code.service.UserService;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UsersMapper;
import hexlet.code.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersMapper mapper;


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

}
