package com.github.maxiamikel.attendancemanagementapi.services;

import java.util.UUID;

import com.github.maxiamikel.attendancemanagementapi.entity.Role;

public interface RoleService {

    public Role findByName(String roleName);

    public Role findById(UUID roleId);
}
