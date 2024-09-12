package hexlet.code.utils;

import hexlet.code.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    private UsersRepository repository;

    public boolean isCurrentUser(Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        var currentUserEmail = authentication.getName();

        return repository.findById(id)
                .map(u -> u.getEmail().equals(currentUserEmail))
                .orElse(false);
    }
}
