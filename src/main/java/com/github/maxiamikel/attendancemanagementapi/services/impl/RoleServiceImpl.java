package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.github.maxiamikel.attendancemanagementapi.entity.Role;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ResourceNotFoundException;
import com.github.maxiamikel.attendancemanagementapi.repository.RoleRepository;
import com.github.maxiamikel.attendancemanagementapi.services.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role", roleName));
    }

    @Override
    public Role findById(UUID roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId.toString()));
    }
}
