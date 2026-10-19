package com.chidicivok.notificationservice.services.implementations;

import com.chidicivok.notificationservice.services.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation implements EmailService {

    private final JavaMailSender javaMailSender;
    private final String senderEmail;

    public EmailServiceImplementation(            JavaMailSender javaMailSender,            @Value("${app.mail.from}") String senderEmail) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
    }

    @Override
    public void sendEmail(String recipient, String subject, String body) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {

            MimeMessageHelper helper =                    new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new MailPreparationException("Failed to prepare HTML email", e);
        }
    }
}