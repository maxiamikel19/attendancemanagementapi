package com.github.maxiamikel.attendancemanagementapi.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.maxiamikel.attendancemanagementapi.entity.Box;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.Role;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.enums.BoxStatus;
import com.github.maxiamikel.attendancemanagementapi.enums.Gender;
import com.github.maxiamikel.attendancemanagementapi.repository.BoxRepository;
import com.github.maxiamikel.attendancemanagementapi.repository.DepartmentRepository;
import com.github.maxiamikel.attendancemanagementapi.repository.RoleRepository;
import com.github.maxiamikel.attendancemanagementapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final BoxRepository boxRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String INIT_ADMIN_ROLE_NAME = "ADMIN";
    private static final String INIT_OPERATOR_ROLE_NAME = "OPERATOR";
    private static final String INIT_DEPARTMENT_NAME = "GENERAL";
    private static final String INIT_BOX_NUMBER = "B-01";
    private static final String INIT_ADMIN_EMAIL = "admin@system.com";
    private static final String INIT_ADMIN_NAME = "Admin";
    private static final String INIT_ADMIN_PASSWORD = "Admin1234*";

    @Bean
    CommandLineRunner initData() {
        return args -> {

            Role adminRole = roleRepository.findByName(INIT_ADMIN_ROLE_NAME)
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name(INIT_ADMIN_ROLE_NAME)
                                    .createdAt(LocalDateTime.now())
                                    .build()));

            Role operatorRole = roleRepository.findByName(INIT_OPERATOR_ROLE_NAME)
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name(INIT_OPERATOR_ROLE_NAME)
                                    .createdAt(LocalDateTime.now())
                                    .build()));

            Department department = departmentRepository.findByName(INIT_DEPARTMENT_NAME)
                    .orElseGet(() -> departmentRepository.save(
                            Department.builder()
                                    .name(INIT_DEPARTMENT_NAME)
                                    .createdAt(LocalDateTime.now())
                                    .build()));

            Box box = boxRepository.findByBoxNumber(INIT_BOX_NUMBER)
                    .orElseGet(() -> boxRepository.save(
                            Box.builder()
                                    .boxNumber(INIT_BOX_NUMBER)
                                    .createdAt(LocalDateTime.now())
                                    .status(BoxStatus.NOT_AVAILABLE)
                                    .build()));

            Optional<User> existingAdmin = userRepository.findByEmail(INIT_ADMIN_EMAIL);
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(operatorRole);

            if (existingAdmin.isEmpty()) {
                User admin = User.builder()
                        .name(INIT_ADMIN_NAME)
                        .email(INIT_ADMIN_EMAIL)
                        .password(passwordEncoder.encode(INIT_ADMIN_PASSWORD))
                        .roles(roles)
                        .department(department)
                        .box(box)
                        .active(true)
                        .emailVerified(true)
                        .birthDate(LocalDate.of(1990, 02, 14))
                        .gender(Gender.MALE)
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(admin);

                log.info("Admin user created");
            } else {
                log.info("ℹAdmin already exists");
            }
        };

    }

}
