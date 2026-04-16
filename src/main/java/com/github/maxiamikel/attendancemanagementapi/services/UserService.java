package com.github.maxiamikel.attendancemanagementapi.services;

import java.util.List;
import java.util.UUID;

import com.github.maxiamikel.attendancemanagementapi.dto.request.UserRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.User;

public interface UserService {

    public User createUser(UserRequest request);

    public User findByEmail(String email);

    User addBox(UUID userId, UUID boxId);

    User updateBox(UUID userId, UUID boxId);

    User removeBox(UUID userId);

    void deleteUser(UUID userId);

    User updateRole(UUID userId, UUID roleId);

    User updateDepartment(UUID userId, UUID departmentId);

    public User findById(UUID userId);

    public List<User> findAll();
}
