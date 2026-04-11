package com.github.maxiamikel.attendancemanagementapi.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.github.maxiamikel.attendancemanagementapi.enums.Gender;
import com.github.maxiamikel.attendancemanagementapi.exceptions.BusinessException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, name = "email", length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Column(nullable = false, name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "is_email_verified", nullable = false)
    private boolean emailVerified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id")
    private Box box;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    public boolean isAccountActivated() {
        return this.active && this.emailVerified;
    }

    public void activate() {
        this.active = true;
        this.emailVerified = true;
    }

    public void removeBox() {
        if (this.box == null) {
            throw new BusinessException("User has no assigned box");
        }
        this.box.unUseBox();
        this.box = null;
    }

    public void assignBox(Box box) {

        if (this.box != null) {
            throw new BusinessException("User already has a box");
        }

        if (!box.isAvailable()) {
            throw new BusinessException("Box is not available");
        }
        this.box = box;
        box.useBox();
    }

    public void changeBox(Box newBox) {

        if (this.box == null) {
            throw new BusinessException("User has no assigned box");
        }

        if (this.box.equals(newBox))
            return;

        if (!newBox.isAvailable()) {
            throw new BusinessException("Box is not available");
        }

        Box oldBox = this.box;
        newBox.useBox();
        this.box = newBox;
        oldBox.unUseBox();
    }

    public void deactivate() {

        if (!this.active) {
            return;
        }

        if (this.box != null) {
            this.box.unUseBox();
            this.box = null;
        }
        this.active = false;
    }

    public void updateRoles(Set<Role> roles) {

        if (roles == null || roles.isEmpty()) {
            throw new BusinessException("User must have at least one role");
        }
        this.roles.clear();
        this.roles.addAll(roles);
    }

    public void changeDepartment(Department newDepartment) {

        if (newDepartment == null) {
            throw new BusinessException("Department cannot be null");
        }

        if (this.box != null) {
            throw new BusinessException("Cannot change department while user has assigned box");
        }

        if (this.department.equals(newDepartment))
            return;

        this.department = newDepartment;
    }
}
