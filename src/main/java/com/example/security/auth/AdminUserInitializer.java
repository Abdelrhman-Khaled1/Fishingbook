package com.example.security.auth;

import com.example.security.user.Role;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {

    @Bean
    public ApplicationRunner initializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if the admin user already exists
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                // Create the admin user
                var adminUser = User.builder()
                        .firstname("Admin")
                        .lastname("User")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("adminPassword"))
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(adminUser);
            }
        };
    }
}
