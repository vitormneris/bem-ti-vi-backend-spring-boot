package com.bemtivi.bemtivi.application.business;

import com.bemtivi.bemtivi.application.domain.email.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailBusiness {
    private final JavaMailSender mailSender;

    public void sendEmail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("bemtivi.autoatendimento@gmail.com");
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getContent());

        mailSender.send(message);
    }
}