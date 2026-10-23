package com.chidicivok.accountservices.services.implementations;

import com.chidicivok.accountservices.clients.AuditClient;
import com.chidicivok.accountservices.clients.CustomerClient;
import com.chidicivok.accountservices.clients.NotificationClient;
import com.chidicivok.accountservices.clients.dto.CreateAuditLogRequest;
import com.chidicivok.accountservices.clients.dto.CreateNotificationRequest;
import com.chidicivok.accountservices.clients.dto.CustomerResponse;
import com.chidicivok.accountservices.dto.requests.CreateAccountRequest;
import com.chidicivok.accountservices.dto.responses.AccountResponse;
import com.chidicivok.accountservices.entities.Account;
import com.chidicivok.accountservices.enums.NotificationType;
import com.chidicivok.accountservices.exception.*;
import com.chidicivok.accountservices.repo.AccountRepo;
import com.chidicivok.accountservices.services.interfaces.AccountService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class AccountServiceImplementation implements AccountService {

    private final AuditClient auditClient;
    private final NotificationClient notificationClient;
    private final AccountRepo accountRepo;
    private final SecureRandom random ;
    private final CustomerClient customerClient;

    public AccountServiceImplementation(AuditClient auditClient, NotificationClient notificationClient, AccountRepo accountRepo,  CustomerClient customerClient) {
        this.auditClient = auditClient;
        this.notificationClient = notificationClient;
        this.random = new SecureRandom();
        this.accountRepo = accountRepo;
        this.customerClient = customerClient;
    }


    // private generate account number
    private String generateAccountNumber() {
        String accountNumber;
        do {

            accountNumber = String.format("%010d", random.nextLong(1_000_000_000L, 10_000_000_000L));

        } while (accountRepo.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    @Override
    public AccountResponse createAccount(CreateAccountRequest request, Long actorId) {

        // check if customer actually exits with that id
        CustomerResponse response = customerClient.getCustomerDetails(request.getCustomerId());



        // check if still active
        if(!response.active()) {
            throw new NotPermittedException("This customer is currently not active | contact admin to change status");
        }

        // check if user already has an account
        if (accountRepo.existsByCustomerId(request.getCustomerId())) {
            throw new DuplicateException("This customer already has an account | Account Limit set to 1");
        }

        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setCustomerId(request.getCustomerId());
        account.setAccountHolderName(response.firstName() + " " + response.lastName());

        // save account details
        Account saved = accountRepo.save(account);

        String targetEmail = response.email();

        log("Created Bank Account",
                "New Bank account created for customer" ,
                actorId,
                "ADMIN",
                "BANK_ACCOUNT_CREATED",
                saved);

        notify(saved,
                targetEmail,
                NotificationType.BANK_ACCOUNT_CREATED,
                "Good day from Civok Bank. " +
                        "\nYour Bank Account has been created" +
                        "\nYour account Number is:  " + saved.getAccountNumber()
                );

        return converToResponse(saved);
    }

    private void notify(Account targetAccount, String targetEmail, NotificationType notificationType, String body) {

        try {

            notificationClient.createAndSendNotification(
                    new CreateNotificationRequest(
                            targetAccount.getCustomerId(),
                            "CUSTOMER",
                            targetAccount.getAccountHolderName(),
                            targetEmail,
                            notificationType,
                            body
                    )
            );

        } catch (NotificationServiceFailure ex) {

            throw new NotificationServiceFailure("Failed to Notify customer");
        }

    }

    private void log(String title, String description, Long actorId, String actorType, String action, Account targetAccount) {

        try {
            auditClient.createLog(new CreateAuditLogRequest(
                    title,
                    description,
                    actorId.toString(),
                    actorType,
                    action,
                    targetAccount.getCustomerId().toString(),
                    "CUSTOMER",
                    "ACCOUNT SERVICE"
            ));
        } catch (AuditLogServiceFailure ex) {
            throw new AuditLogServiceFailure("Failed to log action");
        }

    }


    // private convert to response
    private AccountResponse converToResponse (Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getCustomerId(),
                account.getAccountHolderName(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt());
    }


    @Override
    public AccountResponse balanceEnquiry(Long customerId) {

        Account account = accountRepo.findByCustomerId(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Account not found")
        );

        return converToResponse(account);
    }


}
