package com.chidicivok.adminservice.services.interfaces;

import com.chidicivok.adminservice.dtos.requests.*;
import com.chidicivok.adminservice.dtos.responses.AdminAuthenticationResponse;
import com.chidicivok.adminservice.dtos.responses.AdminResponse;

import java.util.List;

public interface AdminService {

    AdminAuthenticationResponse authenticateAdmin(AdminAuthenticationRequest request);

    AdminResponse createAdmin(  CreateAdminRequest request, Long actorId);

    AdminResponse getAdminById(Long adminId);

    List<AdminResponse> getAllAdmins();

    AdminResponse changeAdminName(Long adminId, ChangeNameAdminRequest request, Long actorId);

    AdminResponse changeAdminPassword(Long adminId, ChangePasswordAdminRequest request , Long actorId);

    AdminResponse changeAdminRole(Long adminId, ChangeRoleAdminRequest request, Long actorId);

    AdminResponse resetAdminPasswordByOwner(Long adminId, ChangePasswordAdminRequest request, Long actorId);
}