package com.chidicivok.accountservices.repo;

import com.chidicivok.accountservices.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByCustomerId(Long CustomerId);

    boolean existsByAccountNumber(String accountNumber);
    boolean existsByCustomerId(Long customerId);
}
