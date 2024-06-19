package com.shopping.example.javamail;

import com.shopping.example.utility.AppProperties;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;
    @Override
    public void sendEmail(String to, String subject, String content) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, "utf-8");
        helper.setFrom(appProperties.getOwnerEmail());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
