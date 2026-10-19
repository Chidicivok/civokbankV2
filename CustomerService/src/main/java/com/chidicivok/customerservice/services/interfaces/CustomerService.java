package com.chidicivok.customerservice.services.interfaces;

import com.chidicivok.customerservice.dto.requests.*;
import com.chidicivok.customerservice.dto.responses.CustomerAuthenticationResponse;
import com.chidicivok.customerservice.dto.responses.CustomerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    CustomerAuthenticationResponse authenticateCustomer(CustomerAuthenticationRequest request);

    CustomerResponse createCustomer(CreateCustomerRequest request, Long actorId);

    CustomerResponse getCustomerById(Long customerId, Long actorId);

    CustomerResponse getCustomerByEmail(String emailId, Long actorId);

    CustomerResponse getCustomerByPhone(String phone, Long actorId);

    List<CustomerResponse> getCustomerByFirstName(String firstName, Long actorId);

    List<CustomerResponse> getCustomerByLastName(String lastName, Long actorId);

    List<CustomerResponse> getCustomerByCity(String city, Long actorId);

    List<CustomerResponse> getAllCustomers(Long actorId);

    CustomerResponse changeCustomerPassword(Long customerId, ChangePasswordCustomerRequest request, Long actorId);

    CustomerResponse changeCustomerEmail(Long customerId, ChangeEmailCustomerRequest request, Long actorId);

    CustomerResponse changeCustomerPhone(Long customerId, ChangePhoneCustomerRequest request, Long actorId);

    CustomerResponse changeCustomerCity(Long customerId, ChangeCityCustomerRequest request, Long actorId);

    CustomerResponse resetCustomerPasswordByStaff(Long customerId, ChangePasswordCustomerRequest request, Long actorId);



}
