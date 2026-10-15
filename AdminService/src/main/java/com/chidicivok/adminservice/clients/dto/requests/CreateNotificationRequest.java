package com.chidicivok.adminservice.clients.dto.requests;

import com.chidicivok.adminservice.enums.NotificationType;
import com.chidicivok.adminservice.enums.TargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateNotificationRequest {

    @NotNull
    private Long targetId;

    @NotNull
    private TargetType targetType;

    @NotBlank
    private String targetEmail;

    @NotNull
    private NotificationType notificationType;

    @NotNull
    private String body;

}
