package com.github.maxiamikel.attendancemanagementapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maxiamikel.attendancemanagementapi.entity.Box;

public interface BoxRepository extends JpaRepository<Box, UUID> {

    Optional<Box> findByBoxNumber(String boxNumber);

    boolean existsByBoxNumber(String boxNumber);

    boolean existsByBoxNumberAndIdNot(String boxNumber, UUID id);
}
