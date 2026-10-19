package com.chidicivok.adminservice.services.implementations;

import com.chidicivok.adminservice.clients.AuditClient;
import com.chidicivok.adminservice.clients.EmailVerifierService;
import com.chidicivok.adminservice.clients.NotificationClient;
import com.chidicivok.adminservice.clients.dto.requests.CreateAuditLogRequest;
import com.chidicivok.adminservice.clients.dto.requests.CreateNotificationRequest;
import com.chidicivok.adminservice.dtos.requests.*;
import com.chidicivok.adminservice.dtos.responses.AdminAuthenticationResponse;
import com.chidicivok.adminservice.dtos.responses.AdminResponse;
import com.chidicivok.adminservice.entities.Admin;
import com.chidicivok.adminservice.enums.NotificationType;
import com.chidicivok.adminservice.enums.TargetType;
import com.chidicivok.adminservice.exceptions.*;
import com.chidicivok.adminservice.repo.AdminRepo;
import com.chidicivok.adminservice.services.interfaces.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImplementations implements AdminService {


    private final AdminRepo adminRepo;
    private final AuditClient auditClient;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerifierService emailVerifierService;
    private final NotificationClient notificationClient;

    public AdminServiceImplementations(AdminRepo adminRepo, AuditClient auditClient, PasswordEncoder passwordEncoder, EmailVerifierService emailVerifierService, NotificationClient notificationClient) {

        this.adminRepo = adminRepo;
        this.auditClient = auditClient;
        this.passwordEncoder = passwordEncoder;
        this.emailVerifierService = emailVerifierService;
        this.notificationClient = notificationClient;
    }


    @Override
    public AdminAuthenticationResponse authenticateAdmin(AdminAuthenticationRequest request) {

        Admin admin = adminRepo.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Admin Username and/or Password is incorrect")
        );

        if (!admin.isActive()) {
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

    @Override
    public AdminResponse createAdmin(CreateAdminRequest request, Long actorId) {

        // ensure to be created admin doesn't exist already
        if (adminRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateException("Admin with email already exits");
        }

        // validate new admin email
        boolean isEmailValid = emailVerifierService.isEmailValid(request.getEmail());

        if (!isEmailValid) {
            throw new InvalidArguementException("Email is not valid | Failed to create admin");
        }

        Admin admin = new Admin();

        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());

        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        admin.setAdminRole(request.getAdminRole());
        admin.setActive(true);

        Admin savedAdmin = adminRepo.save(admin);


        try {

            // AUDIT
            log("Create Administrator",
                    "A new Admin Credentials has been created",
                    actorId,
                    "CREATE_ADMIN",
                    savedAdmin
            );

        } catch (AuditLogServiceFailure ex) {
            throw new AuditLogServiceFailure("Failed to Log newly created Admin");
        }

        try {
            // send notification
            notify(savedAdmin,
                    NotificationType.ADMIN_ACCOUNT_CREATED,
                    "Welcome to CivokBank, Your account credentials have been created \nUsername: " + savedAdmin.getEmail() + "\nPassword: " + request.getPassword() + "\n Be advised to change your password");


        } catch (NotificationServiceFailure e) {
            throw new NotificationServiceFailure("Failed to notify Recipient");
        }

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


    private void notify(Admin targetAdmin, NotificationType notificationType, String body) {

        notificationClient.createAndSendNotification(
                new CreateNotificationRequest(
                        targetAdmin.getId(),
                        TargetType.ADMIN,
                        targetAdmin.getFirstName() + " " + targetAdmin.getLastName(),
                        targetAdmin.getEmail(),
                        notificationType,
                        body
                )
        );

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

        try {
            log("Change Administrator Name(S)",
                    "Administrator name has been changed from \"" + previousFirstName + " " + previousLastName + "\"\nTo " + saved.getFirstName() + " " + saved.getLastName(),
                    actorId,
                    "CHANGE_NAME",
                    saved
            );

        } catch (AuditLogServiceFailure ex) {
            throw new AuditLogServiceFailure("Failed to Log ");
        }

        try {
            notify(admin, NotificationType.ADMIN_NAME_CHANGED, "Owner at Civok Bank has changed your name to " +  saved.getFirstName() + " " + saved.getLastName());


        } catch (NotificationServiceFailure e) {
            throw new NotificationServiceFailure("Failed to notify Recipient");
        }
        return convertToResponse(saved, "Name changed successfully");

    }

    @Override
    public AdminResponse changeAdminPassword(Long adminId, ChangePasswordAdminRequest request, Long actorId) {

        Admin admin = findAdmin(adminId);

        if (!Objects.equals(actorId, admin.getId())) {
            throw new NotPermittedException("This account does not belong to you");
        }

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));

        Admin saved = adminRepo.save(admin);


        try {
            log("Change Administrator Password",
                    "Administrator password has been changed ",
                    actorId,
                    "CHANGE_PASSWORD",
                    saved
            );

        } catch (AuditLogServiceFailure ex) {
            throw new AuditLogServiceFailure("Failed to Log ");
        }

        try {


            notify(admin, NotificationType.ADMIN_PASSWORD_CHANGED, "You have changed your password");

        } catch (NotificationServiceFailure e) {
            throw new NotificationServiceFailure("Failed to notify Recipient");
        }

        return convertToResponse(saved, "Password Changed Successfully");
    }

    @Override
    public AdminResponse changeAdminRole(Long adminId, ChangeRoleAdminRequest request, Long actorId) {

        Admin admin = findAdmin(adminId);

        String previousRole = admin.getAdminRole().name();

        admin.setAdminRole(request.getAdminRole());

        Admin saved = adminRepo.save(admin);

        try {
            log("Change Administrator Role",
                    "Administrator role has been changed from \"" + previousRole + "\"",
                    actorId,
                    "CHANGE_ROLE",
                    saved
            );

        } catch (AuditLogServiceFailure ex) {
            throw new AuditLogServiceFailure("Failed to Log ");
        }


        try {
            notify(admin, NotificationType.ADMIN_ROLE_CHANGED, "Owner at CivokBank has changed your Role");


        } catch (NotificationServiceFailure e) {
            throw new NotificationServiceFailure("Failed to notify Recipient");
        }
        return convertToResponse(saved, "Admin Role changed successfully");
    }

    @Override
    public AdminResponse resetAdminPasswordByOwner(Long adminId, ChangePasswordAdminRequest request, Long actorId) {

        Admin admin = findAdmin(adminId);

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));

        Admin saved = adminRepo.save(admin);


        try {
            log("Reset Administrator Password",
                    "Administrator password has been changed by Owner ",
                    actorId,
                    "RESET_PASSWORD",
                    saved
            );

        } catch (AuditLogServiceFailure ex) {
            throw new AuditLogServiceFailure("Failed to Log ");
        }

        try {
            notify(admin, NotificationType.ADMIN_PASSWORD_RESET, "Owner at Civok Bank has reset your password.\nThe new password is " + request.getNewPassword() + "\nBe advised to change this password");

        } catch (NotificationServiceFailure e) {
            throw new NotificationServiceFailure("Failed to notify Recipient");
        }
        return convertToResponse(saved, "Password Reset Successful");


    }
}
