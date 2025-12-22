package dev.cacassiano.workout_tracker.DTOs.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "Username can't be blank")
    private String username;

    @NotBlank(message = "Email can't be blank")
    @Email
    private String email;

    @NotBlank(message = "Password can't be blank")
    @Length(min = 8, message = "The min length of the password is 8")
    private String password;
}
