package com.github.maxiamikel.attendancemanagementapi.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.DepartmentCounter;
import com.github.maxiamikel.attendancemanagementapi.repository.DepartmentCounterRepository;
import com.github.maxiamikel.attendancemanagementapi.services.DepartmentCounterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentCounterServiceImpl implements DepartmentCounterService {

    private final DepartmentCounterRepository departmentCounterRepository;

    @Override
    public Long getNextNumberByDepartment(Department department) {

        DepartmentCounter departmentCounter = departmentCounterRepository.findByDepartmentName(department)
                .orElseGet(() -> {

                    DepartmentCounter newDepartmentCounter = DepartmentCounter.builder()
                            .department(department)
                            .lastNumber(0L)
                            .build();

                    return departmentCounterRepository.save(newDepartmentCounter);
                });

        departmentCounter.setLastNumber(departmentCounter.getLastNumber() + 1);
        departmentCounterRepository.save(departmentCounter);
        return departmentCounter.getLastNumber();
    }
}
