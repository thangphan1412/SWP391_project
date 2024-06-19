package com.shopping.example.security;

import com.shopping.example.entity.Account;
import com.shopping.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Transactional
public class MyUserDetailService implements UserDetailsService, UserDetailsPasswordService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        var userEntity = accountRepository.findByEmail(user.getUsername());
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        userEntity.get().setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(userEntity.get());
        return new MyUserDetails(userEntity.get());
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isEmpty()) {
            throw new UsernameNotFoundException("User " + email + " was not found in the database");
        } else {
            return new MyUserDetails(account.get());
        }
    }

}
