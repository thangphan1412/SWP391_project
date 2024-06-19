package com.shopping.example.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPassword {
    private String newPassWord;
    private String token;
}
