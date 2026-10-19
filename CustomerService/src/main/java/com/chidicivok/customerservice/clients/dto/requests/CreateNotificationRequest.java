package com.chidicivok.customerservice.clients.dto.requests;

import com.chidicivok.customerservice.enums.NotificationType;
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
    private String targetType;

    @NotBlank
    private String targetName;

    @NotBlank
    private String targetEmail;

    @NotNull
    private NotificationType notificationType;

    @NotNull
    private String body;

}
