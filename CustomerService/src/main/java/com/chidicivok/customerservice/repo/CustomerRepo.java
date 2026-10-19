package com.chidicivok.customerservice.repo;

import com.chidicivok.customerservice.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Customer> findByPhone(String phone);
    boolean existsByPhone(String phone);

    List<Customer> findByFirstName(String firstName);
    List<Customer> findByLastName(String LastName);
    List<Customer> findByCity(String city);
}
