package com.chidicivok.adminservice.config;

import com.chidicivok.adminservice.entities.Admin;
import com.chidicivok.adminservice.enums.AdminRole;
import com.chidicivok.adminservice.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class AdminBootstrapConfig {

    @Value("${owner.firstname}")
    private String firstName;

    @Value("${owner.lastname}")
    private String lastName;

    @Value("${owner.email}")
    private String email;

    @Value("${owner.password}")
    private String password;

    @Bean
    public CommandLineRunner createOwner(AdminRepo adminRepo, PasswordEncoder passwordEncoder) {

        return args -> {

            if(!adminRepo.existsByEmail(email)) {
                Admin admin = new Admin();

                admin.setFirstName(firstName);
                admin.setLastName(lastName);
                admin.setPassword(passwordEncoder.encode(password));
                admin.setEmail(email);
                admin.setAdminRole(AdminRole.OWNER);
                admin.setActive(true);

                adminRepo.save(admin);
            }

        };
    }
}
