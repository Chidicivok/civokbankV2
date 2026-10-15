package com.chidicivok.notificationservice.dto.requests;

import com.chidicivok.notificationservice.enums.NotificationType;
import com.chidicivok.notificationservice.enums.TargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
