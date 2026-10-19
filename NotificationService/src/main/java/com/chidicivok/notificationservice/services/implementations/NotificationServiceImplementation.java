package com.chidicivok.notificationservice.services.implementations;


import com.chidicivok.notificationservice.dto.requests.CreateNotificationRequest;
import com.chidicivok.notificationservice.dto.responses.NotificationResponse;
import com.chidicivok.notificationservice.entities.Notification;
import com.chidicivok.notificationservice.enums.NotificationStatus;
import com.chidicivok.notificationservice.exceptions.BadRequestException;
import com.chidicivok.notificationservice.repos.NotificationRepo;
import com.chidicivok.notificationservice.services.interfaces.EmailService;
import com.chidicivok.notificationservice.services.interfaces.NotificationService;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImplementation implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    public NotificationServiceImplementation(NotificationRepo notificationRepo, EmailService emailService, EmailTemplateService emailTemplateService) {
        this.notificationRepo = notificationRepo;
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    public NotificationResponse createAndSendNotification(CreateNotificationRequest request) {

        Notification notification = new Notification();

        notification.setTargetId(request.getTargetId());
        notification.setTargetType(request.getTargetType());
        notification.setTargetName(request.getTargetName());
        notification.setNotificationType(request.getNotificationType());
        notification.setBody(request.getBody());
        notification.setNotificationStatus(NotificationStatus.PENDING);
        notification.setSendAttempt(0);
        notification.setSuccessfulAttempt(0);

        notificationRepo.save(notification);


        // attempt to send it
        notification.setSendAttempt(notification.getSendAttempt() + 1);

        try {

            String htmlBody = emailTemplateService.buildEmail(request.getTargetName(),   request.getBody());

            emailService.sendEmail(request.getTargetEmail(), request.getNotificationType().name(), htmlBody);

            LocalDateTime sentNow = LocalDateTime.now();

            notification.setNotificationStatus(NotificationStatus.SENT);

            notification.setSuccessfulAttempt(notification.getSuccessfulAttempt() + 1);

            if (notification.getFirstSentAt() == null) {
                notification.setFirstSentAt(sentNow);
            }

            notification.setLastSentAt(sentNow);

            // save notification again to update
            notificationRepo.save(notification);

            // response
            return response(notification);


        } catch (MailException e) {

            notification.setNotificationStatus(NotificationStatus.FAILED);
            notificationRepo.save(notification);
            throw new BadRequestException("Failed to send email to recipient");
        }

    }


    // private helper for response
    private NotificationResponse response(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTargetId(),
                notification.getTargetType(),
                notification.getNotificationType(),
                notification.getBody(),
                notification.getNotificationStatus(),
                notification.getSendAttempt(),
                notification.getSuccessfulAttempt(),
                notification.getFirstSentAt(),
                notification.getLastSentAt()

        );
    }

}
