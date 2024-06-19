package com.shopping.example.DTO.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {
    @Email(message = "Email không hợp lệ")
    private String email;
}
