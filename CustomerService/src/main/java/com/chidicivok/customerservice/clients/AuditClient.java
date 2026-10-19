package com.chidicivok.customerservice.clients;


import com.chidicivok.customerservice.clients.dto.requests.CreateAuditLogRequest;
import com.chidicivok.customerservice.exception.AuditLogServiceFailure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class AuditClient {


    private final RestClient restClient;

    public AuditClient(@Value(("${audit.service.base}")) String auditServiceBaseUrl) {
        this.restClient = RestClient.create(auditServiceBaseUrl);
    }

    public void createLog(CreateAuditLogRequest request) {
       try {
           restClient.post()
                   .uri("/create-log")
                   .body(request)
                   .retrieve()
                   .toBodilessEntity();
       } catch (RestClientException e) {
           throw new AuditLogServiceFailure("Failed to communicate with Audit Services");
       }
    }


}

