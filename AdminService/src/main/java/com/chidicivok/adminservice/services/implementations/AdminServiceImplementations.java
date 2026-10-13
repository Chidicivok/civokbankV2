package com.chidicivok.adminservice.services.implementations;

import com.chidicivok.adminservice.clients.AuditClient;
import com.chidicivok.adminservice.clients.dto.CreateAuditLogRequest;
import com.chidicivok.adminservice.dtos.requests.*;
import com.chidicivok.adminservice.dtos.responses.AdminAuthenticationResponse;
import com.chidicivok.adminservice.dtos.responses.AdminResponse;
import com.chidicivok.adminservice.entities.Admin;
import com.chidicivok.adminservice.exceptions.DuplicateException;
import com.chidicivok.adminservice.exceptions.InvalidArguementException;
import com.chidicivok.adminservice.exceptions.NotPermittedException;
import com.chidicivok.adminservice.exceptions.ResourceNotFoundException;
import com.chidicivok.adminservice.repo.AdminRepo;
import com.chidicivok.adminservice.services.interfaces.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImplementations implements AdminService {


    private final AdminRepo adminRepo;
    private final AuditClient auditClient;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImplementations(AdminRepo adminRepo, AuditClient auditClient, PasswordEncoder passwordEncoder) {

        this.adminRepo = adminRepo;
        this.auditClient = auditClient;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AdminAuthenticationResponse authenticateAdmin(AdminAuthenticationRequest request) {

        Admin admin = adminRepo.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Admin Username and/or Password is incorrect")
        );

        if(!admin.isActive()) {
            throw new NotPermittedException("Admin is currently inactive");
        }

        boolean matches = passwordEncoder.matches(request.getPassword(), admin.getPassword());

        if (!matches) {
            throw new InvalidArguementException("Admin Username and/or Password is incorrect");
        }

        return new AdminAuthenticationResponse(
                admin.getId(), admin.getEmail(), admin.getAdminRole()
        );
    }

    @Transactional
    @Override
    public AdminResponse createAdmin(CreateAdminRequest request, Long actorId) {


        // ensure to be created admin doesn't exist already
        if (adminRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateException("Admin with email already exits");
        }

        Admin admin = new Admin();

        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());

        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        admin.setAdminRole(request.getAdminRole());
        admin.setActive(true);

        Admin savedAdmin = adminRepo.save(admin);

        // AUDIT
        log("Create Administrator",
                "A new Admin Credentials has been created",
                actorId,
                "CREATE_ADMIN",
                savedAdmin
        );

        return convertToResponse(savedAdmin, "Admin Created Successfully");
    }


    private void log(String title, String description, Long actorId, String action, Admin targetAdmin) {

        auditClient.createLog(new CreateAuditLogRequest(
                title,
                description,
                actorId.toString(),
                "ADMIN",
                action,
                targetAdmin.getId().toString(),
                "ADMIN",
                "ADMIN SERVICE"
        ));

    }


    // private helper method for admin esponse
    private AdminResponse convertToResponse(Admin admin, String message) {
        return new AdminResponse(
                admin.getId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getEmail(),
                admin.getAdminRole(),
                admin.isActive(),
                admin.getCreatedAt(),
                admin.getUpdatedAt(),
                message
        );
    }


    // private helper find admin
    private Admin findAdmin(Long id) {
        return adminRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Admin not found")
        );
    }

    @Override
    public AdminResponse getAdminById(Long adminId) {

        return convertToResponse(findAdmin(adminId), "Admin Retrieved successfully");
    }

    @Override
    public List<AdminResponse> getAllAdmins() {
        return adminRepo.findAll()
                .stream()
                .map(
                        returnedAdmin -> convertToResponse(returnedAdmin, "Admin retrieved successfully")
                )
                .toList();
    }

    @Override
    public AdminResponse changeAdminName(Long adminId, ChangeNameAdminRequest request, Long actorId) {

        Admin admin = findAdmin(adminId);

        String previousFirstName = admin.getFirstName();
        String previousLastName = admin.getLastName();

        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        Admin saved = adminRepo.save(admin);

        log("Change Administrator Name(S)",
                "Administrator name has been changed from \"" + previousFirstName + " " + previousLastName + "\"",
                actorId,
                "CHANGE_NAME",
                saved
        );

        return convertToResponse(saved, "Name changed successfully");
    }

    @Override
    public AdminResponse changeAdminPassword(Long adminId, ChangePasswordAdminRequest request, Long actorId) {

        Admin admin = findAdmin(adminId);

        if(!Objects.equals(actorId, admin.getId())) {
            throw new NotPermittedException("This account does not belong to you");
        }

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));

        Admin saved = adminRepo.save(admin);

        log("Change Administrator Password",
                "Administrator password has been changed ",
                actorId,
                "CHANGE_PASSWORD",
                saved
        );

        return convertToResponse(saved, "Password Changed Successfully");
    }

    @Override
    public AdminResponse changeAdminRole(Long adminId, ChangeRoleAdminRequest request, Long actorId) {

        Admin admin = findAdmin(adminId);

        String previousRole = admin.getAdminRole().name();

        admin.setAdminRole(request.getAdminRole());

        Admin saved = adminRepo.save(admin);

        log("Change Administrator Role",
                "Administrator role has been changed from \"" + previousRole + "\"",
                actorId,
                "CHANGE_ROLE",
                saved
        );

        return convertToResponse(saved, "Admin Role changed successfully");
    }

    @Override
    public AdminResponse resetAdminPasswordByOwner(Long adminId, ChangePasswordAdminRequest request, Long actorId) {

        Admin admin = findAdmin(adminId);

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));

        Admin saved = adminRepo.save(admin);

        log("Reset Administrator Password",
                "Administrator password has been changed by Owner ",
                actorId,
                "RESET_PASSWORD",
                saved
        );

        return convertToResponse(saved, "Password Reset Successful");


    }
}
