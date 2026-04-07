package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.maxiamikel.attendancemanagementapi.dto.request.UserRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.Role;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.exceptions.DuplicatedResourceException;
import com.github.maxiamikel.attendancemanagementapi.repository.UserRepository;
import com.github.maxiamikel.attendancemanagementapi.services.DepartmentService;
import com.github.maxiamikel.attendancemanagementapi.services.NotificationService;
import com.github.maxiamikel.attendancemanagementapi.services.RoleService;
import com.github.maxiamikel.attendancemanagementapi.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String BASIC_ROLE_NAME = "OPERATOR";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final NotificationService notificationService;

    @Override
    public User createAccount(UserRequest request) {

        log.info("Registering user with email: {}", request.getEmail());

        validateUserDoesNotExist(request.getEmail());

        Department department = departmentService.findByName(request.getDepartment());
        Role role = roleService.findByName(BASIC_ROLE_NAME);

        User user = buildUser(request, department, role);

        User newUser = userRepository.save(user);

        // send email activación
        notificationService.createActivationAccountNotification(newUser.getEmail());

        return newUser;

    }

    private void validateUserDoesNotExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicatedResourceException("User already exists with email: " + email);
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private User buildUser(UserRequest request, Department department, Role role) {

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodePassword(request.getPassword()))
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .department(department)
                .roles(Set.of(role))
                .active(false)
                .emailVerified(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
