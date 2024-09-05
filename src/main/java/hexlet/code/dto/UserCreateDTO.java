package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String password;
}
