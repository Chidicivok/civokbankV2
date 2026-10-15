package com.chidicivok.notificationservice.services.interfaces;


public interface EmailService {

    void sendEmail(String recipient,String subject, String body);
}
