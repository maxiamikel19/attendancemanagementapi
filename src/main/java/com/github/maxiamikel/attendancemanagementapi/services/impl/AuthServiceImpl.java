package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.dto.request.AuthRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.AccessToken;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.exceptions.AlreadyActiveException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.CredentialException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ResourceNotFoundException;
import com.github.maxiamikel.attendancemanagementapi.repository.UserRepository;
import com.github.maxiamikel.attendancemanagementapi.security.JwtService;
import com.github.maxiamikel.attendancemanagementapi.services.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AccessToken login(AuthRequest request) {

        User user = userRepository.findByEmailWithRoles(request.getEmail())
                .orElseThrow(() -> new CredentialException("Invalid username or password"));

        if (!user.isAccountActivated()) {
            throw new CredentialException("Please, check your email and activate your account.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CredentialException("Invalid username or password");
        }

        return jwtService.generateToken(user);
    }

    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    @Transactional
    public void activateNewAccount(UUID id) {

        User user = getById(id);

        if (user.isAccountActivated()) {
            throw new AlreadyActiveException("Your account already activated");
        }
        user.activate();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
    }

}
