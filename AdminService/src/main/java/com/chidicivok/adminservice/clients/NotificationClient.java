package com.chidicivok.adminservice.clients;

import com.chidicivok.adminservice.clients.dto.requests.CreateNotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NotificationClient {

    private final RestClient restClient;


    public NotificationClient(@Value("${Notification.service.url}") String notificationServiceUrl ) {
        this.restClient = RestClient.create(notificationServiceUrl);
    }

    public void createAndSendNotification(CreateNotificationRequest request) {

        restClient.post()
                .uri("/send")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }


}
