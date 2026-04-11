package com.github.maxiamikel.attendancemanagementapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.DepartmentCounter;

import jakarta.persistence.LockModeType;

public interface DepartmentCounterRepository extends JpaRepository<DepartmentCounter, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM DepartmentCounter d WHERE d.department = :department")
    Optional<DepartmentCounter> findByDepartmentName(Department department);

    @Modifying
    @Query("DELETE FROM DepartmentCounter")
    int resetDepartmentCounter();
}
