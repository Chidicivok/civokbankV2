package com.chidicivok.auditservice.controllers;

import com.chidicivok.auditservice.dto.requests.CreateAuditLogRequest;
import com.chidicivok.auditservice.dto.responses.AuditLogResponse;
import com.chidicivok.auditservice.services.interfaces.AuditLogService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping("/create-log")
    public ResponseEntity<AuditLogResponse> createLog(@Valid @RequestBody CreateAuditLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditLogService.createLog(request));
    }

    @GetMapping("/get-log-by-id/{id}")
    public ResponseEntity<AuditLogResponse> getLogById(@PathVariable Long id) {
        return ResponseEntity.ok(auditLogService.getLogById(id));
    }

    @GetMapping("/get-log-by-actor/{id}")
    public ResponseEntity<List<AuditLogResponse>> getLogByActorId(@PathVariable String id) {
        return ResponseEntity.ok(auditLogService.getLogByActorId(id));
    }

    @GetMapping("/get-log-by-actor-type/{actorType}")
    public ResponseEntity<List<AuditLogResponse>> getLogByActorType(@PathVariable String actorType){
        return ResponseEntity.ok(auditLogService.getLogByActorType(actorType));
    }

    @GetMapping("/get-log-by-target/{id}")
    public ResponseEntity<List<AuditLogResponse>> getLogByTargetId(@PathVariable String id) {
        return ResponseEntity.ok(auditLogService.getLogByTargetId(id));
    }


    @GetMapping("/get-log-by-target-type/{targetType}")
    public ResponseEntity<List<AuditLogResponse>> getLogByTargetType(@PathVariable String targetType){
        return ResponseEntity.ok(auditLogService.getLogByTargetType(targetType));
    }


    @GetMapping("/get-log-by-service-name/{name}")
    public ResponseEntity<List<AuditLogResponse>> getLogByServiceName(@PathVariable String name){
        return ResponseEntity.ok(auditLogService.getLogByServiceName(name));
    }

    @GetMapping("/get-log-by-action/{action}")
    public ResponseEntity<List<AuditLogResponse>> getLogByAction(@PathVariable String action){
        return ResponseEntity.ok(auditLogService.getLogByAction(action));
    }

    @GetMapping("/get-log-by-date/{date}")
    public ResponseEntity<List<AuditLogResponse>> getLogByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return ResponseEntity.ok(auditLogService.getLogByDate(date));
    }

    @GetMapping("/get-all-logs")
    public ResponseEntity<List<AuditLogResponse>> getAllLogs(){
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

}
