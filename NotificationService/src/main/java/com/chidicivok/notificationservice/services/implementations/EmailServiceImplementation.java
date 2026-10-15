package com.chidicivok.notificationservice.services.implementations;

import com.chidicivok.notificationservice.services.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation implements EmailService {

    private final JavaMailSender javaMailSender;
    private final String senderEmail;

    public EmailServiceImplementation(JavaMailSender javaMailSender, @Value("${app.mail.from}") String senderEmail) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
    }


    @Override
    public void sendEmail(String recipient, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);


    }
}
