package com.shopping.example.javamail;


import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendEmail( String to, String subject, String content) throws MessagingException;
}
