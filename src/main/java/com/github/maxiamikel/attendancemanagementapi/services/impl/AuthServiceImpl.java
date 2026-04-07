package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.dto.request.AuthRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.AccessToken;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.exceptions.AlreadyActiveException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ResourceNotFoundException;
import com.github.maxiamikel.attendancemanagementapi.repository.UserRepository;
import com.github.maxiamikel.attendancemanagementapi.services.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public AccessToken login(AuthRequest request) {
        return null;
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

    private User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
    }
}
