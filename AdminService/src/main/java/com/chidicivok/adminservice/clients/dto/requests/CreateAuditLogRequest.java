package com.chidicivok.adminservice.clients.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAuditLogRequest {

    private String title;
    private String description;
    private String actorId;
    private String actorType;
    private String action;
    private String targetId;
    private String targetType;
    private String serviceName;
}