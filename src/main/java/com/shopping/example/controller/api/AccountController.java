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

import com.shopping.example.utility.Contant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/account")
@CrossOrigin("*")
public class AccountController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private JwtService tokenService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private  AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    @PostMapping("/login")
    public ModelAndView login(@Valid @ModelAttribute LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("login"); // Nếu đăng nhập thất bại sẽ quay lại trang đăng nhập
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

            Map<String, Object> claims = new HashMap<>();
            claims.put("email", userDetails.getAccount().getEmail());
            claims.put("userId", userDetails.getAccount().getId());

            String token = jwtService.generateToken(claims, 15 * 60 * 1000);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            LoginResponse loginResponse;

            modelAndView.setViewName("redirect:/"); // Nếu đăng nhập thành công sẽ chuyển hướng về trang chủ

            if (userDetails.getAccount() != null) {
                loginResponse = new LoginResponse(token, userDetails.getAccount().getEmail(), roles);

                request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

                Cookie cookie = new Cookie("JWT_TOKEN", token);
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(15 * 60 * 1000)); // 15 minutes
                cookie.setPath("/");
                response.addCookie(cookie);

                if (roles.contains(Contant.ROLE_ADMIN)) {
                    cookie = new Cookie("1234abc", "1234");
                    cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(15 * 60 * 1000)); // 15 minutes
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                if (roles.contains(Contant.ROLE_USER)) {
                    cookie = new Cookie("4567abc", "4567");
                    cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(15 * 60 * 1000)); // 15 minutes
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                if (roles.contains(Contant.ROLE_EMPLOYEE)) {
                    cookie = new Cookie("89abc", "89");
                    cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(15 * 60 * 1000)); // 15 minutes
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        } catch (AuthenticationException e) {
            modelAndView.addObject("errorMessage", "invalid email or password");
        }
        return modelAndView;
    }

    @PostMapping("/forgot-password")
    public void createForgotPassword(@RequestParam(name = "email") String email, HttpServletResponse response) throws IOException {
        var command = new ForgotPasswordRequest(email);
        accountService.forgotPassword(command);
        response.sendRedirect("/forgotPasswordSuccess");
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam(name = "token", required = false) String token,
                              @RequestParam(name = "newPassword", required = false) String newPassword,
                              HttpServletResponse response) throws IOException {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(token, newPassword);
        accountService.resetPassword(resetPasswordRequest);
        response.sendRedirect("/resetPasswordSuccess");
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

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request){
//        String token = tokenService.getTokenFromRequest(request);
//        tokenService.invalidateToken(token);
//        return ResponseEntity.ok("logout success");
//    }
}

