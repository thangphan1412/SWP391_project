package com.shopping.example.service;

import com.shopping.example.DTO.request.ForgotPasswordRequest;
import com.shopping.example.DTO.request.ResetPasswordRequest;
import com.shopping.example.entity.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    boolean existsByEmail(String email);
    void forgotPassword (ForgotPasswordRequest forgotPasswordRequest);
    void resetPassword (ResetPasswordRequest resetPasswordRequest);

    Account getCurrentAccount();
    Account findByEmail(String email);


}
