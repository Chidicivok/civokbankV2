package com.chidicivok.auditservice.services.implementations;

import com.chidicivok.auditservice.dto.requests.CreateAuditLogRequest;
import com.chidicivok.auditservice.dto.responses.AuditLogResponse;
import com.chidicivok.auditservice.entities.AuditLog;
import com.chidicivok.auditservice.exceptions.NotFoundException;
import com.chidicivok.auditservice.repo.AuditLogRepo;
import com.chidicivok.auditservice.services.interfaces.AuditLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditLogServiceImplementation implements AuditLogService {


    private final AuditLogRepo auditLogRepo;

    public AuditLogServiceImplementation(AuditLogRepo auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }


    // helper mapper
    private AuditLogResponse convertToResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getTitle(),
                log.getDescription(),
                log.getActorId(),
                log.getActorType(),
                log.getAction(),
                log.getTargetId(),
                log.getTargetType(),
                log.getServiceName(),
                log.getCreatedAt()
        );
    }


    @Override
    public AuditLogResponse createLog(CreateAuditLogRequest request) {

        AuditLog log = new AuditLog();

        log.setTitle(request.getTitle());
        log.setDescription(request.getDescription());
        log.setActorId(request.getActorId());
        log.setActorType(request.getActorType());
        log.setAction(request.getAction());
        log.setTargetId(request.getTargetId());
        log.setTargetType(request.getTargetType());
        log.setServiceName(request.getServiceName());

        AuditLog savedLog = auditLogRepo.save(log);

        return convertToResponse(savedLog);

    }

    @Override
    public AuditLogResponse getLogById(Long id) {

        AuditLog log = auditLogRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Log with ID: \"" + id + "\" not found")
        );
        return convertToResponse(log);
    }

    @Override
    public List<AuditLogResponse> getAllLogs() {
        List<AuditLog> logList = auditLogRepo.findAll();
        List<AuditLogResponse> responses = new ArrayList<>();
        for (AuditLog log : logList) {
            responses.add(convertToResponse(log));
        }

        return responses;
    }

    @Override
    public List<AuditLogResponse> getLogByActorId(String actorId) {

        List<AuditLog> logList = auditLogRepo.findByActorId(actorId);

        List<AuditLogResponse> responses = new ArrayList<>();
        for (AuditLog log : logList) {
            responses.add(convertToResponse(log));
        }

        return responses;
    }

    @Override
    public List<AuditLogResponse> getLogByActorType(String actorType) {

        List<AuditLog> logList = auditLogRepo.findByActorType(actorType);

        List<AuditLogResponse> responses = new ArrayList<>();
        for (AuditLog log : logList) {
            responses.add(convertToResponse(log));
        }

        return responses;
    }

    @Override
    public List<AuditLogResponse> getLogByTargetId(String targetId) {

        List<AuditLog> logList = auditLogRepo.findByTargetId(targetId);

        List<AuditLogResponse> responses = new ArrayList<>();
        for (AuditLog log : logList) {
            responses.add(convertToResponse(log));
        }

        return responses;
    }

    @Override
    public List<AuditLogResponse> getLogByTargetType(String targetType) {

        List<AuditLog> logList = auditLogRepo.findByTargetType(targetType);

        List<AuditLogResponse> responses = new ArrayList<>();
        for (AuditLog log : logList) {
            responses.add(convertToResponse(log));
        }

        return responses;
    }

    @Override
    public List<AuditLogResponse> getLogByServiceName(String serviceName) {

        List<AuditLog> logList = auditLogRepo.findByServiceName(serviceName);

        List<AuditLogResponse> responses = new ArrayList<>();
        for (AuditLog log : logList) {
            responses.add(convertToResponse(log));
        }

        return responses;
    }

    @Override
    public List<AuditLogResponse> getLogByDate(LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<AuditLog> logList = auditLogRepo.findByCreatedAtBetween(start, end);

        List<AuditLogResponse> responses = new ArrayList<>();

        for (AuditLog log : logList) {
            responses.add(convertToResponse(log));
        }

        return responses;
    }


    @Override
    public List<AuditLogResponse> getLogByAction(String action) {

        List<AuditLog> logList = auditLogRepo.findByAction(action);

        List<AuditLogResponse> responses = new ArrayList<>();
        for (AuditLog log : logList) {
            responses.add(convertToResponse(log));
        }

        return responses;
    }

}
