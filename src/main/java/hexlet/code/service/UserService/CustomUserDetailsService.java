package hexlet.code.service.UserService;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsManager {

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
        var hashedPassword = passwordEncoder.encode(userData.getPassword());
        user.setPasswordDigest(hashedPassword);
        usersRepository.save(user);
    }

    //пока есть вопрос нужно ли и если да то верно ли реализована логика переопределения методов ниже
    @Override
    public void updateUser(UserDetails userdata) {
        var user = usersRepository.findByEmail(userdata.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User: " + userdata + " NOT FOUND"));
        user.setEmail(userdata.getUsername());
        var newHashPassword = userdata.getPassword();
        user.setPasswordDigest(passwordEncoder.encode(newHashPassword));
        usersRepository.save(user);
        // throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    @Override
    public void deleteUser(String username) {
        var user = usersRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User: " + username + " NOT FOUND"));
        usersRepository.delete(user);
        //throw new UnsupportedOperationException("Unimplemented method 'userExists'");
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
