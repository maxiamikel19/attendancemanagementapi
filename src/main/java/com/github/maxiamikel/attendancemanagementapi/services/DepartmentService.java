package com.github.maxiamikel.attendancemanagementapi.services;

import java.util.List;
import java.util.UUID;

import com.github.maxiamikel.attendancemanagementapi.dto.request.DepartmentRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;

public interface DepartmentService {

    Department create(DepartmentRequest request);

    Department findByName(String name);

    Department findById(UUID departmentId);

    Department update(DepartmentRequest request, UUID id);

    void delete(UUID id);

    List<Department> findAll();
}
