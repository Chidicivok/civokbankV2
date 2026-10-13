package com.chidicivok.adminservice.controllers;

import com.chidicivok.adminservice.dtos.requests.ChangeNameAdminRequest;
import com.chidicivok.adminservice.dtos.requests.ChangePasswordAdminRequest;
import com.chidicivok.adminservice.dtos.requests.ChangeRoleAdminRequest;
import com.chidicivok.adminservice.dtos.requests.CreateAdminRequest;
import com.chidicivok.adminservice.dtos.responses.AdminResponse;
import com.chidicivok.adminservice.services.interfaces.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/owner/admin")
public class OwnerAdminController {

    private final AdminService adminService;

    public OwnerAdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // private helper method for actor id
    private Long getActorId( Jwt jwt) {
        return  ((Number) jwt.getClaim("adminId")).longValue();
    }


    @PostMapping("/create-admin")
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody CreateAdminRequest request, @AuthenticationPrincipal Jwt jwt ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(request, getActorId(jwt)));
    }

    @PatchMapping("/change-admin-name/{id}")
    public ResponseEntity<AdminResponse> changeName(@PathVariable Long id,@Valid @RequestBody ChangeNameAdminRequest request, @AuthenticationPrincipal Jwt jwt) {
        return  ResponseEntity.ok(adminService.changeAdminName(id, request, getActorId(jwt)));
    }


    @PatchMapping("/change-role/{id}")
    public ResponseEntity<AdminResponse> changeRole(@PathVariable Long id, @Valid @RequestBody ChangeRoleAdminRequest request, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(adminService.changeAdminRole(id, request, getActorId(jwt)));
    }

    @PatchMapping("/reset-password-by-owner/{id}")
    public ResponseEntity<AdminResponse> resetPassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordAdminRequest request, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(adminService.resetAdminPasswordByOwner(id, request, getActorId(jwt)));
    }

}
