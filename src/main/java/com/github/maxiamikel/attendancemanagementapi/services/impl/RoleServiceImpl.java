package com.github.maxiamikel.attendancemanagementapi.services.impl;

import org.springframework.stereotype.Service;

import com.github.maxiamikel.attendancemanagementapi.entity.Role;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ResourceNotFoundException;
import com.github.maxiamikel.attendancemanagementapi.repository.RoleRepository;
import com.github.maxiamikel.attendancemanagementapi.services.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role", roleName));
    }
}
