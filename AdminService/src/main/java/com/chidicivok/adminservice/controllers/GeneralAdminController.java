package com.chidicivok.adminservice.controllers;

import com.chidicivok.adminservice.dtos.requests.ChangePasswordAdminRequest;
import com.chidicivok.adminservice.dtos.responses.AdminResponse;
import com.chidicivok.adminservice.services.interfaces.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/general/admin")
public class GeneralAdminController {

    private final AdminService adminService;

    public GeneralAdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/find-admin/{id}")
    public ResponseEntity<AdminResponse> findAdminById ( @PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @GetMapping("/get-all-admins")
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }


    // private helper method for actor id
    private Long getActorId(Jwt jwt) {
        return ((Number) jwt.getClaim("adminId")).longValue();
    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity<AdminResponse> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordAdminRequest request,@AuthenticationPrincipal Jwt jwt){

        return ResponseEntity.ok(adminService.changeAdminPassword(id, request, getActorId(jwt)));

    }

}
