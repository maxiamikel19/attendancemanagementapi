package com.github.maxiamikel.attendancemanagementapi.services;

import java.util.UUID;

import com.github.maxiamikel.attendancemanagementapi.dto.request.AuthRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.AccessToken;
import com.github.maxiamikel.attendancemanagementapi.entity.User;

public interface AuthService {

    AccessToken login(AuthRequest request);

    public User getCurrentUser();

    public void activateNewAccount(UUID id);

    User findByEmail(String email);
}
