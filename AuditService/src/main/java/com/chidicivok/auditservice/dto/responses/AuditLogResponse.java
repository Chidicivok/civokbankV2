package com.chidicivok.auditservice.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class AuditLogResponse {

    private Long id;
    private String title;
    private String description;
    private String actorId;
    private String actorType;
    private String action;
    private String targetId;
    private String targetType;
    private String serviceName;
    private LocalDateTime createdAt;
}
