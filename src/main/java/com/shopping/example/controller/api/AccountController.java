package com.shopping.example.controller.api;

import com.shopping.example.DTO.request.ChangePasswordRequest;
import com.shopping.example.DTO.request.ForgotPasswordRequest;
import com.shopping.example.DTO.request.LoginRequest;
import com.shopping.example.DTO.request.ResetPasswordRequest;
import com.shopping.example.DTO.response.LoginResponse;

import com.shopping.example.security.MyUserDetails;
import com.shopping.example.security.jwt.JwtService;
import com.shopping.example.service.AccountService;
import com.shopping.example.service.CustomerService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private  AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result , HttpServletRequest request, HttpServletResponse response) {
        // Lấy thông tin người dùng từ form đăng nhập

        // check
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // lay ra thong tin nguoi dung dnag dang nhap
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        // tao token , claim : luu thong tin token dua tren 2 colum
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getAccount().getEmail());
        claims.put("userId", userDetails.getAccount().getId());
        //for 15 minutes, sau khi het time thi phai dang nhap lai
        String token = jwtService.generateToken(claims, 15 * 60 * 1000);
        // tra ra thong tin
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse loginResponse;

        if (userDetails.getAccount() != null) {
            loginResponse = new LoginResponse(token ,
                    userDetails.getAccount().getEmail() ,roles);

            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            // Lưu token vào cookie
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(15 * 60 * 1000)); // 15 minutes
            cookie.setPath("/"); // Đảm bảo rằng cookie có thể được truy cập trên mọi đường dẫn
            response.addCookie(cookie);
            return ResponseEntity.ok(loginResponse);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> createForgotPassword(@RequestParam(name = "email") String email) {
        var command = new ForgotPasswordRequest(email);
        accountService.forgotPassword(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam(name = "token", required = false) String token,
                                                 @RequestParam(name = "newPassword", required = false) String newPassword) {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(token, newPassword);
        accountService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Thay đổi mật khẩu thành công");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePasswords(
            @RequestParam(name = "currentPassword", required = false) String currentPassword,
            @RequestParam(name = "newPassword", required = false) String newPassword,
            @RequestParam(name = "confirmNewPassword", required = false) String confirmNewPassword,
            Principal principal){
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(currentPassword, newPassword, confirmNewPassword);
        accountService.changePassword(changePasswordRequest, principal);
        return ResponseEntity.ok("Thay doi mat khau thanh cong");
    }
}
