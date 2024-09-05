package hexlet.code.component;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.repository.UsersRepository;
import hexlet.code.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var adminData = new UserCreateDTO();
        adminData.setEmail(email);
        adminData.setPassword("qwerty");
        adminData.setFirstName("admin");
        usersService.create(adminData);
    }
}
