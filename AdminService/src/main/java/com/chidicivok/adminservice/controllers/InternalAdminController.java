package com.chidicivok.adminservice.controllers;

import com.chidicivok.adminservice.dtos.requests.AdminAuthenticationRequest;
import com.chidicivok.adminservice.dtos.responses.AdminAuthenticationResponse;
import com.chidicivok.adminservice.services.interfaces.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/admin")
public class InternalAdminController {

    private final AdminService adminService;


    public InternalAdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    // to be used by all admins - owner, staff and auditors

    @PostMapping("/authenticate-admin")
    public ResponseEntity<AdminAuthenticationResponse> authenticateAdmin(@Valid @RequestBody AdminAuthenticationRequest request){
        return ResponseEntity.ok(adminService.authenticateAdmin(request));
    }
}
