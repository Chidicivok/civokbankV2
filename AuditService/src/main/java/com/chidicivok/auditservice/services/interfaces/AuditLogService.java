package com.chidicivok.auditservice.services.interfaces;

import com.chidicivok.auditservice.dto.requests.CreateAuditLogRequest;
import com.chidicivok.auditservice.dto.responses.AuditLogResponse;

import java.time.LocalDate;
import java.util.List;

public interface AuditLogService {

    AuditLogResponse createLog(CreateAuditLogRequest request);
    AuditLogResponse getLogById( Long id);
    List<AuditLogResponse> getAllLogs();
    List<AuditLogResponse> getLogByActorId(String actorId);
    List<AuditLogResponse> getLogByActorType(String actorType);
    List<AuditLogResponse> getLogByTargetId(String targetId);
    List<AuditLogResponse> getLogByTargetType(String targetType);
    List<AuditLogResponse> getLogByServiceName(String serviceName);
    List<AuditLogResponse> getLogByDate(LocalDate date);
    List<AuditLogResponse> getLogByAction(String action);
}
