package com.shopping.example.service;



import com.shopping.example.DTO.request.ChangePasswordRequest;
import com.shopping.example.DTO.request.ForgotPasswordRequest;
import com.shopping.example.DTO.request.ResetPasswordRequest;
import com.shopping.example.entity.Account;

import java.security.Principal;
import java.util.List;

public interface AccountService {

    boolean existsByEmail(String email);
    void forgotPassword (ForgotPasswordRequest forgotPasswordRequest);
    void resetPassword (ResetPasswordRequest resetPasswordRequest);

    Account getCurrentAccount();
    Account findByEmail(String email);


    List<Account> getAllAccountsWithRoles();

    void changePassword(ChangePasswordRequest changePasswordRequest, Principal principal);


    void replaceRoleAccount(Long accountId, Long newRoleId);
}
