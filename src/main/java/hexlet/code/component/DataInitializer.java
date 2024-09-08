package hexlet.code.component;

import hexlet.code.model.User;
import hexlet.code.repository.UsersRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final CustomUserDetailsService usersService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var adminData = new User();
        adminData.setEmail(email);
        adminData.setPasswordDigest("qwerty");
        adminData.setFirstName("admin");
        usersService.createUser(adminData);
        var user = usersRepository.findByEmail(email).get();
    }
}
