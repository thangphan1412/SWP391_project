package com.shopping.example.service.impl;

import com.shopping.example.DTO.request.ChangePasswordRequest;
import com.shopping.example.DTO.request.ForgotPasswordRequest;
import com.shopping.example.DTO.request.ResetPasswordRequest;
import com.shopping.example.entity.Account;
import com.shopping.example.entity.Role;
import com.shopping.example.javamail.MailService;
import com.shopping.example.repository.AccountRepository;
import com.shopping.example.repository.RoleRepository;
import com.shopping.example.security.MyUserDetailService;
import com.shopping.example.security.MyUserDetails;
import com.shopping.example.security.jwt.JwtService;
import com.shopping.example.service.AccountService;
import com.shopping.example.utility.AppProperties;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class AccountServiceImpl implements AccountService {


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private MailService mailService;
    @Autowired
    private MyUserDetailService myUserDetailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsPasswordService userDetailsService;

    public AccountServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        var account = accountRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (account.isEmpty()) {
            throw new RuntimeException("Không tìm thấy email");
        }
        Map<String , Object> claims = new HashMap<>();
        claims.put("email", account.get().getEmail());
        claims.put("userId" , account.get().getId());

        String token = jwtService.generateToken(claims, 15*60*1000);
        var to = account.get().getEmail();
        var subject = "Reset Password" ;
        var url = appProperties.getHost() + "/resetPassword?token=" + token;
        var content = "Nhấn vào link sau để reset password: " + url;
        CompletableFuture.runAsync(() -> {
            try {
                mailService.sendEmail(to, subject, content);
            }
            catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String email ;
        try {
            email = jwtService.getValue(resetPasswordRequest.getToken(), c-> c.get("email", String.class));
            if (email == null || email.isEmpty()) {
                throw new RuntimeException("Token không hợp lệ");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        }
        var account = myUserDetailService.loadUserByUsername(email);
        userDetailsService.updatePassword(account, resetPasswordRequest.getNewPassword());
    }

    @Override
    public Account getCurrentAccount() {
        // Lấy đối tượng Authentication từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MyUserDetails) {
            // Ép kiểu principal về MyUserDetails để lấy thông tin tài khoản
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

            // Lấy thông tin tài khoản từ MyUserDetails
            return userDetails.getAccount();
        }

        // Trường hợp không tìm thấy hoặc không đăng nhập, trả về null
        return null;
    }

    @Override
    public Account findByEmail(String email) {
        return   accountRepository.findByEmail(email).get();
    }

    @Override
    public List<Account> getAllAccountsWithRoles() {
        return accountRepository.findAllWithRoles();
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
    }

    @Override
    @Transactional
    public void replaceRoleAccount(Long accountId, Long newRoleId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        Optional<Role> roleOpt = roleRepository.findById(newRoleId);

        if (accountOpt.isPresent() && roleOpt.isPresent()) {
            Account account = accountOpt.get();
            Role newRole = roleOpt.get();
            account.getRoles().clear();
            account.getRoles().add(newRole);
            accountRepository.save(account);
        } else {
            throw new RuntimeException("Account or Role not found");
        }


    }


}
