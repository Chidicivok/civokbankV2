package com.chidicivok.auditservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_audit_log")
@NoArgsConstructor
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String title;

    @Column(updatable = false)
    private String description;

    @Column(name = "actor_id", nullable = false, updatable = false)
    private String actorId;

    @Column(name = "actor_type", nullable = false, updatable = false)
    private String actorType;

    @Column(nullable = false, updatable = false)
    private String action;

    @Column(name = "target_id", updatable = false)
    private String targetId;

    @Column(name = "target_type", updatable = false)
    private String targetType;

    @Column(name = "service_name", nullable = false, updatable = false)
    private String serviceName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }


}
