package dev.cacassiano.workout_tracker.DTOs.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Must be a well formed email")
    private String email;

    @NotBlank(message = "Password can't be blank")
    @Length(min = 8, message = "The min length 8 characters")
    private String password;
}
