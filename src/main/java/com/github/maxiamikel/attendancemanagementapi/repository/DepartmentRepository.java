package com.github.maxiamikel.attendancemanagementapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maxiamikel.attendancemanagementapi.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    Optional<Department> findByName(String departmentName);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
}
