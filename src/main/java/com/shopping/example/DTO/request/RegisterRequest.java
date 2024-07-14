package com.shopping.example.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Name cannot be blank")
    @Length(max = 30, message = "Name cannot be longer than 30 characters")
    private String name;
    @Email(message = "Invalid email address")
    private String email;
    @Length(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;
    private String role;
}
