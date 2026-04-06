package com.github.maxiamikel.attendancemanagementapi.services;

import com.github.maxiamikel.attendancemanagementapi.entity.Department;

public interface DepartmentCounterService {

    public Long getNextNumberByDepartment(Department department);
}
