package hexlet.code.controller.api.users;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exception.ForbiddenException;
import hexlet.code.exception.UnauthorizedException;
import hexlet.code.service.UsersService;
import hexlet.code.utils.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping
  //  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getList() {
        return usersService.getAll();
    }

    @GetMapping("/{id}")
 //   @PreAuthorize("@userUtils.isCurrentUser(#id) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable Long id) {
        return usersService.getUser(id);
    }

    @PostMapping
 //   @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        return usersService.create(userCreateDTO);
    }

    @PutMapping("/{id}")
 //   @PreAuthorize("@userUtils.isCurrentUser(#id) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@RequestBody @Valid UserUpdateDTO updateDTO, @PathVariable Long id) {
        return usersService.update(updateDTO, id);
    }

    @DeleteMapping("/{id}")
   // @PreAuthorize("@userUtils.isCurrentUser(#id) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        usersService.delete(id);
    }
}
