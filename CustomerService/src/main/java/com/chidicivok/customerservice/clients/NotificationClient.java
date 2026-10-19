package com.chidicivok.customerservice.clients;

import com.chidicivok.customerservice.clients.dto.requests.CreateNotificationRequest;
import com.chidicivok.customerservice.exception.NotificationServiceFailure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class NotificationClient {

    private final RestClient restClient;


    public NotificationClient(@Value("${Notification.service.url}") String notificationServiceUrl ) {
        this.restClient = RestClient.create(notificationServiceUrl);
    }

    public void createAndSendNotification(CreateNotificationRequest request) {

        try {
            restClient.post()
                    .uri("/send")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            System.out.println("Reached notification services ok");

        } catch (RestClientException e) {
            throw new NotificationServiceFailure("Failed to connect to notification services");
        }
    }


}
