package com.assettracking.config;

import com.assettracking.model.User;
import com.assettracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@assettracking.com")) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@assettracking.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }

        if (!userRepository.existsByEmail("user@assettracking.com")) {
            User user = User.builder()
                    .name("Rinsin")
                    .email("user@assettracking.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(User.Role.USER)
                    .build();
            userRepository.save(user);
        }
    }
}
