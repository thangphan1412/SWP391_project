package com.shopping.example.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private List<String> roles;

    public LoginResponse(String token, String email, List<String> roles) {
        this.token = token;
        this.email = email;
        this.roles = roles;
    }
}
