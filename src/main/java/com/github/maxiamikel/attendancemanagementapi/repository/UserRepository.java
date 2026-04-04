package com.github.maxiamikel.attendancemanagementapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.maxiamikel.attendancemanagementapi.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
