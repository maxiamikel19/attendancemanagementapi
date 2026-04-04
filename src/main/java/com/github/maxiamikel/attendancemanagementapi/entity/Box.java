package com.github.maxiamikel.attendancemanagementapi.entity;

import com.github.maxiamikel.attendancemanagementapi.enums.BoxStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "boxes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Box extends BaseEntity {

    @Column(nullable = false, unique = true, name = "box_number", length = 10)
    private String boxNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "box_status", nullable = false)
    private BoxStatus status;
}
