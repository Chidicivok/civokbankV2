package com.chidicivok.notificationservice.services.interfaces;

import com.chidicivok.notificationservice.dto.requests.CreateNotificationRequest;
import com.chidicivok.notificationservice.dto.responses.NotificationResponse;

public interface NotificationService {

    NotificationResponse createAndSendNotification(CreateNotificationRequest request);
}
