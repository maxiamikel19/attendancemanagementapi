package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.dto.request.DepartmentRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.exceptions.BusinessException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.DuplicatedResourceException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ResourceNotFoundException;
import com.github.maxiamikel.attendancemanagementapi.repository.DepartmentRepository;
import com.github.maxiamikel.attendancemanagementapi.services.DepartmentService;
import com.github.maxiamikel.attendancemanagementapi.utils.ApiUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
// @PreAuthorize("hasRole('ADMIN')")
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Department create(DepartmentRequest request) {

        String normalizedName = ApiUtils.normalizeStringToUpperCase(request.getName());

        validateDepartmentDoesNotExist(normalizedName);

        Department department = buildDepartment(normalizedName);

        log.info("Creating department with name: {}", normalizedName);

        return departmentRepository.save(department);
    }

    @Override
    public Department findByName(String name) {

        String normalizedName = ApiUtils.normalizeStringToUpperCase(name);

        return departmentRepository.findByName(normalizedName)
                .orElseThrow(() -> new ResourceNotFoundException("Department", normalizedName));
    }

    @Override
    public Department findById(UUID departmentId) {
        return getById(departmentId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Department update(DepartmentRequest request, UUID id) {

        Department department = getById(id);

        String normalizedName = ApiUtils.normalizeStringToUpperCase(request.getName());

        if (departmentRepository.existsByNameAndIdNot(normalizedName, id)) {
            throw new DuplicatedResourceException("Department", normalizedName);
        }

        log.info("Updating department {} to new name {}", department.getName(), normalizedName);

        department.setName(normalizedName);

        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {

        Department department = getById(id);

        log.info("Deleting department with id: {}", id);
        if (!department.getUsers().isEmpty()) {
            throw new BusinessException("Department cannot be deleted due has relacionated users");
        }

        departmentRepository.delete(department);
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    private void validateDepartmentDoesNotExist(String name) {
        if (departmentRepository.existsByName(name)) {
            throw new DuplicatedResourceException("Department", name);
        }
    }

    private Department buildDepartment(String name) {
        return Department.builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Department getById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id.toString()));
    }

}
