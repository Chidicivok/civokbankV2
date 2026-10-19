package com.chidicivok.notificationservice.entities;


import com.chidicivok.notificationservice.enums.NotificationStatus;
import com.chidicivok.notificationservice.enums.NotificationType;
import com.chidicivok.notificationservice.enums.TargetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "targetType", nullable = false)
    private TargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "target_name", nullable = false)
    private String targetName;

    @Column(name = "body", nullable = false)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus notificationStatus;

    @Column(name = "send_attempt", nullable = false)
    private Integer sendAttempt;

    @Column(name = "successful_attempt")
    private Integer successfulAttempt;

    @Column(name = "first_sent_at")
    private LocalDateTime firstSentAt;

    @Column(name = "last_sent_at")
    private LocalDateTime lastSentAt;


    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", targetId=" + targetId +
                ", targetType=" + targetType +
                ", notificationType=" + notificationType +
                ", body='" + body + '\'' +
                ", notificationStatus=" + notificationStatus +
                ", sendAttempt=" + sendAttempt +
                ", successfulAttempt=" + successfulAttempt +
                ", firstSentAt=" + firstSentAt +
                ", lastSentAt=" + lastSentAt +
                '}';
    }
}
