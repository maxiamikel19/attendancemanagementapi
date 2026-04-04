package com.github.maxiamikel.attendancemanagementapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maxiamikel.attendancemanagementapi.entity.DepartmentCounter;

public interface DepartmentCounterRepository extends JpaRepository<DepartmentCounter, Long> {

}
