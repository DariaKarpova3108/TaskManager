package hexlet.code.utils;

import hexlet.code.model.User;
import hexlet.code.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    @Autowired
    private UsersRepository repository;

    public boolean isCurrentUser(Long id) {
        var authentiation = SecurityContextHolder.getContext().getAuthentication();
        if (authentiation == null || !authentiation.isAuthenticated()) {
            return false;
        }
        var currentUserEmail = authentiation.getName();

        return repository.findById(id)
                .map(u-> u.getEmail().equals(currentUserEmail))
                .orElse(false);
    }
}
