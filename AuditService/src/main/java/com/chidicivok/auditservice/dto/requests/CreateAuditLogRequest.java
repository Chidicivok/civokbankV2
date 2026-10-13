package com.chidicivok.auditservice.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAuditLogRequest {


    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String actorId;

    @NotBlank
    private String actorType;

    @NotBlank
    private String action;


    private String targetId;

    private String targetType;

    @NotBlank
    private String serviceName;


}
