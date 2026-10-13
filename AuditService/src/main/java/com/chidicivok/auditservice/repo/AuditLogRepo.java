package com.chidicivok.auditservice.repo;

import com.chidicivok.auditservice.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByActorId(String actorId);
    List<AuditLog> findByActorType(String actorType);
    List<AuditLog> findByTargetId(String targetId);
    List<AuditLog> findByTargetType(String targetType);
    List<AuditLog> findByServiceName(String serviceName);
    List<AuditLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
