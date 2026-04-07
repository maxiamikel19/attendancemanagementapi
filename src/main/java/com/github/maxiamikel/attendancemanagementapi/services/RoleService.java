package com.github.maxiamikel.attendancemanagementapi.services;

import com.github.maxiamikel.attendancemanagementapi.entity.Role;

public interface RoleService {

    public Role findByName(String roleName);
}
