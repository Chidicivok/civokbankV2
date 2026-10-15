package com.chidicivok.adminservice.clients;


import com.chidicivok.adminservice.clients.dto.requests.CreateAuditLogRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuditClient {


    private final RestClient restClient;

    public AuditClient(@Value(("${audit.service.base}")) String auditServiceBaseUrl) {
        this.restClient = RestClient.create(auditServiceBaseUrl);
    }

    public void createLog(CreateAuditLogRequest request) {
        restClient.post()
                .uri("/create-log")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }


}

