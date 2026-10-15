package com.chidicivok.notificationservice.dto.responses;

import com.chidicivok.notificationservice.enums.NotificationStatus;
import com.chidicivok.notificationservice.enums.NotificationType;
import com.chidicivok.notificationservice.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long targetId;
    private TargetType targetType;
    private NotificationType notificationType;
    private String body;
    private NotificationStatus notificationStatus;
    private Integer sendAttempt;
    private Integer successfulAttempt;
    private LocalDateTime firstSent;
    private LocalDateTime lastSent;


}
