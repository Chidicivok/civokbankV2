package com.chidicivok.customerservice.services.implementations;

import com.chidicivok.customerservice.clients.AuditClient;
import com.chidicivok.customerservice.clients.EmailVerifierService;
import com.chidicivok.customerservice.clients.NotificationClient;
import com.chidicivok.customerservice.clients.dto.requests.CreateAuditLogRequest;
import com.chidicivok.customerservice.clients.dto.requests.CreateNotificationRequest;
import com.chidicivok.customerservice.dto.requests.*;
import com.chidicivok.customerservice.dto.responses.CustomerAuthenticationResponse;
import com.chidicivok.customerservice.dto.responses.CustomerResponse;
import com.chidicivok.customerservice.entities.Customer;
import com.chidicivok.customerservice.enums.NotificationType;
import com.chidicivok.customerservice.exception.*;
import com.chidicivok.customerservice.repo.CustomerRepo;
import com.chidicivok.customerservice.services.interfaces.CustomerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepo customerRepo;
    private final AuditClient auditClient;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerifierService emailVerifierService;
    private final NotificationClient notificationClient;

    public CustomerServiceImplementation(CustomerRepo customerRepo, AuditClient auditClient, PasswordEncoder passwordEncoder, EmailVerifierService emailVerifierService, NotificationClient notificationClient) {
        this.customerRepo = customerRepo;
        this.auditClient = auditClient;
        this.passwordEncoder = passwordEncoder;
        this.emailVerifierService = emailVerifierService;
        this.notificationClient = notificationClient;
    }


    @Override
    public CustomerAuthenticationResponse authenticateCustomer(CustomerAuthenticationRequest request) {

        Customer customer = customerRepo.findByEmail(request.getEmail()).orElseThrow(
                () -> new InvalidArguementException("Customer Username and/or Password is incorrect")
        );

        if (!customer.isActive()) {
            throw new NotPermittedException("Customer is currently inactive");
        }

        boolean matches = passwordEncoder.matches(request.getPassword(), customer.getPassword());

        if (!matches) {
            throw new InvalidArguementException("Customer Username and/or Password is incorrect");
        }

        return new CustomerAuthenticationResponse(customer.getId(), customer.getEmail());
    }


    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request, Long actorId) {

        // ensure to be created customer doesn't exist already
        if (customerRepo.existsByEmail(request.getEmail())) {
            throw new DuplicateException("Customer with email already exits");
        }

        if (customerRepo.existsByPhone(request.getPhone())) {
            throw new DuplicateException("Customer with phone already exits");
        }

        // validate new customer email
        boolean isEmailValid = emailVerifierService.isEmailValid(request.getEmail());

        if (!isEmailValid) {
            throw new InvalidArguementException("Email is not valid | Failed to create customer");
        }

        Customer customer = new Customer();

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setCity(request.getCity());
        customer.setActive(true);

        Customer savedCustomer = customerRepo.save(customer);

        // AUDIT
        log("Create Customer",
                "A new Customer Credentials has been created",
                actorId,
                "ADMIN",
                "CREATE_CUSTOMER",
                savedCustomer
        );

        // send notification
        notify(savedCustomer,
                NotificationType.CUSTOMER_ACCOUNT_CREATED,
                "Welcome to CivokBank, Your account credentials have been created" +
                        "\nUsername: " + savedCustomer.getEmail() +
                        "\nPassword: " + request.getPassword() +
                        "\n Be advised to change your password");


        return convertToResponse(savedCustomer, "Customer Created Successfully");
    }


    // private helper method for admin esponse
    private CustomerResponse convertToResponse(Customer customer, String message) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCity(),
                customer.isActive(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                message
        );
    }

    private void notify(Customer targetCustomer, NotificationType notificationType, String body) {

        try {
            System.out.println("started notification process");
            notificationClient.createAndSendNotification(
                    new CreateNotificationRequest(
                            targetCustomer.getId(),
                            "CUSTOMER",
                            targetCustomer.getFirstName() + " " + targetCustomer.getLastName(),
                            targetCustomer.getEmail(),
                            notificationType,
                            body
                    )
            );
            System.out.println("ended notification process");
        } catch (NotificationServiceFailure ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new NotificationServiceFailure("Failed to Notify customer");
        }

    }

    private void log(String title, String description, Long actorId, String actorType, String action, Customer targetCustomer) {

        try {
            auditClient.createLog(new CreateAuditLogRequest(
                    title,
                    description,
                    actorId.toString(),
                    actorType,
                    action,
                    targetCustomer.getId().toString(),
                    "CUSTOMER",
                    "CUSTOMER SERVICE"
            ));
        } catch (AuditLogServiceFailure ex) {
            throw new AuditLogServiceFailure("Failed to log action");
        }

    }

    @Override
    public CustomerResponse getCustomerById(Long customerId, Long actorId) {
        return convertToResponse(customerRepo.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found")
        ), "Customer Found successfully");
    }

    @Override
    public CustomerResponse getCustomerByEmail(String emailId, Long actorId) {
        return convertToResponse(customerRepo.findByEmail(emailId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found")
        ), "Customer Found successfully");
    }

    @Override
    public CustomerResponse getCustomerByPhone(String phone, Long actorId) {
        return convertToResponse(customerRepo.findByPhone(phone).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found")
        ), "Customer Found successfully");
    }

    @Override
    public List<CustomerResponse> getCustomerByFirstName(String firstName, Long actorId) {
        List<Customer> customerList = customerRepo.findByFirstName(firstName);
        List<CustomerResponse> responses = new ArrayList<>();
        for (Customer customer : customerList) {
            responses.add(convertToResponse(customer, "Customer Retrieved"));
        }
        return responses;
    }

    @Override
    public List<CustomerResponse> getCustomerByLastName(String lastName, Long actorId) {
        List<Customer> customerList = customerRepo.findByLastName(lastName);
        List<CustomerResponse> responses = new ArrayList<>();
        for (Customer customer : customerList) {
            responses.add(convertToResponse(customer, "Customer Retrieved"));
        }
        return responses;
    }

    @Override
    public List<CustomerResponse> getCustomerByCity(String city, Long actorId) {
        List<Customer> customerList = customerRepo.findByCity(city);
        List<CustomerResponse> responses = new ArrayList<>();
        for (Customer customer : customerList) {
            responses.add(convertToResponse(customer, "Customer Retrieved"));
        }
        return responses;
    }

    @Override
    public List<CustomerResponse> getAllCustomers(Long actorId) {
        List<Customer> customerList = customerRepo.findAll();
        List<CustomerResponse> responses = new ArrayList<>();

        for (Customer customer : customerList) {
            responses.add(convertToResponse(customer, "Customer Retrieved"));
        }
        return responses;
    }

    @Override
    public CustomerResponse changeCustomerPassword(Long customerId, ChangePasswordCustomerRequest request, Long actorId) {

        Customer customer = customerRepo.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found")
        );

        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        Customer savedCustomer = customerRepo.save(customer);

        log("Change Password",
                "Customer changed their password",
                actorId,
                "CUSTOMER",
                "CHANGE_PASSWORD",
                savedCustomer
        );


        // send notification
        notify(savedCustomer,
                NotificationType.CUSTOMER_PASSWORD_CHANGED,
                "Welcome to CivokBank, Your account password has been changed ");


        return convertToResponse(savedCustomer, "Customer Password changed successfully");
    }

    @Override
    public CustomerResponse changeCustomerEmail(Long customerId, ChangeEmailCustomerRequest request, Long actorId) {

        Customer customer = customerRepo.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found")
        );

        String previousEmail = customer.getEmail();

        if (previousEmail.equalsIgnoreCase(request.getEmail()) || customerRepo.existsByEmail(request.getEmail())) {
            throw new DuplicateException("Customer with email already exits.");
        }

        if (!emailVerifierService.isEmailValid(request.getEmail())) {
            throw new ExternalEmailApiException("Failed to verify email address");
        }

        customer.setEmail(request.getEmail());
        Customer savedCustomer = customerRepo.save(customer);


        log("Change Email",
                "Customer changed their Email",
                actorId,
                "CUSTOMER",
                "CHANGE_EMAIL",
                savedCustomer
        );


        // send notification
        notify(savedCustomer,
                NotificationType.CUSTOMER_EMAIL_CHANGED,
                "Welcome to CivokBank, Your account email has been changed from \"" + previousEmail + "\"");

        return convertToResponse(savedCustomer, "Customer email changed successfully");

    }

    @Override
    public CustomerResponse changeCustomerPhone(Long customerId, ChangePhoneCustomerRequest request, Long actorId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found")
        );

        String previousPhoneNumber = customer.getPhone();

        if (previousPhoneNumber.equalsIgnoreCase(request.getPhone()) || customerRepo.existsByPhone(request.getPhone())) {
            throw new DuplicateException("Customer with phone number already exists");
        }

        customer.setPhone(request.getPhone());
        Customer savedCustomer = customerRepo.save(customer);

        log("Change Phone Number",
                "Customer changed their Phone number",
                actorId,
                "CUSTOMER",
                "CHANGE_PHONE_NUMBER",
                savedCustomer
        );

        // send notification
        notify(savedCustomer,
                NotificationType.CUSTOMER_PHONE_CHANGED,
                "Welcome to CivokBank, Your phone number has been changed from \"" + previousPhoneNumber + "\"");

        return convertToResponse(savedCustomer, "Customer Phone number changed successfully");

    }

    @Override
    public CustomerResponse changeCustomerCity(Long customerId, ChangeCityCustomerRequest request, Long actorId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found")
        );

        String previousCity = customer.getCity();

        customer.setCity(request.getCity());
        Customer savedCustomer = customerRepo.save(customer);

        log("Change City",
                "Customer changed their city",
                actorId,
                "CUSTOMER",
                "CHANGE_CITY",
                savedCustomer
        );

        notify(savedCustomer,
                NotificationType.CUSTOMER_CITY_CHANGED,
                "Welcome to CivokBank, Your city has been changed ");

        return convertToResponse(savedCustomer, "Customer city changed successfully");

    }

    @Override
    public CustomerResponse resetCustomerPasswordByStaff(Long customerId, ChangePasswordCustomerRequest request, Long actorId) {

        // verify actor ??? here or elsewhere

        Customer customer = customerRepo.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found")
        );

        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        Customer savedCustomer = customerRepo.save(customer);

        log("Reset Password",
                "Customer password was reset by admin: " + actorId,
                actorId,
                "ADMIN",
                "RESET_PASSWORD",
                savedCustomer
        );

        notify(savedCustomer,
                NotificationType.CUSTOMER_PASSWORD_RESET,
                "Welcome to CivokBank, Your account password has been reset to : " +
                        "\n\"" + request.getPassword() +
                        "\"\nBe advised to change this password");

        return convertToResponse(savedCustomer, "Customer Password was reset by admin " + actorId);

    }
}
