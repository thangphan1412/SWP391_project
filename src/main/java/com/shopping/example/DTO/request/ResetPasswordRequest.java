package com.shopping.example.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    private String token;

    @Length(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String newPassword;
}
