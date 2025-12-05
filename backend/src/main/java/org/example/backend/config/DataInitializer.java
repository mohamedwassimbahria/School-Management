package org.example.backend.config;

import lombok.RequiredArgsConstructor;
import org.example.backend.entity.Admin;
import org.example.backend.repository.AdminRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            String encodedPassword = passwordEncoder.encode("password");
            admin.setPassword(encodedPassword);
            adminRepository.save(admin);
            logger.info("Created default admin user with username: admin and hashed password: {}", encodedPassword);
        }
    }
}
