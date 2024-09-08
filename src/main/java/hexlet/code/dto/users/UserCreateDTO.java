package hexlet.code.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UserCreateDTO {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    @Size(min = 3)
    private String password;
}
