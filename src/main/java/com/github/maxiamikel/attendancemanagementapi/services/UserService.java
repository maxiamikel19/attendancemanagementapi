package com.github.maxiamikel.attendancemanagementapi.services;

import java.util.UUID;

import com.github.maxiamikel.attendancemanagementapi.dto.request.UserRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.User;

public interface UserService {

    public User createAccount(UserRequest request);

    public User findByEmail(String email);

    User assignBox(UUID userId, UUID boxId);

    User changeBox(UUID userId, UUID boxId);

    User removeBox(UUID userId);

    void deleteUser(UUID userId);

    User changeRole(UUID userId, UUID roleId);

    User changeDepartment(UUID userId, UUID departmentId);

    public User fingById(UUID userId);
}
