package hexlet.code.dto.users;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserDTO {
    private Long id;
    private String firstName;
    private String email;
    private String password;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
