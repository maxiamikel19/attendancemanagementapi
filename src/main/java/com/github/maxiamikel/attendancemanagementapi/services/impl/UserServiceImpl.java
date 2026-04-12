package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.dto.request.UserRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Box;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.Role;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.exceptions.BusinessException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.DuplicatedResourceException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ResourceNotFoundException;
import com.github.maxiamikel.attendancemanagementapi.repository.UserRepository;
import com.github.maxiamikel.attendancemanagementapi.services.BoxService;
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
    private final BoxService boxService;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
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

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", email));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User assignBox(UUID userId, UUID boxId) {
        User user = getById(userId);
        Box box = boxService.findById(boxId);
        user.assignBox(box);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User changeBox(UUID userId, UUID boxId) {
        User user = getById(userId);
        Box newBox = boxService.findById(boxId);
        user.changeBox(newBox);
        return userRepository.save(user);

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User removeBox(UUID userId) {
        User user = getById(userId);
        user.removeBox();
        return userRepository.save(user);
    }

    @Override
    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(UUID userId) {
        User user = getById(userId);

        if (!user.isActive()) {
            throw new BusinessException("User has no an active account");
        }

        if (user.getBox() != null) {
            user.removeBox();
        }
        user.deactivate();
        userRepository.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User changeRole(UUID userId, UUID roleId) {
        User user = getById(userId);
        Role role = roleService.findById(roleId);

        user.updateRoles(Set.of(role));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User changeDepartment(UUID userId, UUID departmentId) {
        User user = getById(userId);
        Department department = departmentService.findById(departmentId);

        user.changeDepartment(department);

        return userRepository.save(user);
    }

    private void validateUserDoesNotExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicatedResourceException("User already exists with email: " + email);
        }
    }

    private User getById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
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
