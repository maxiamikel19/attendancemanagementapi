package com.github.maxiamikel.attendancemanagementapi.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.request.DepartmentRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.DepartmentResponse;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.DepartmentMapper;
import com.github.maxiamikel.attendancemanagementapi.services.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> create(@Valid @RequestBody DepartmentRequest request) {

        var department = departmentService.create(request);
        var response = departmentMapper.toResponse(department);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseFactory.created(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> update(@Valid @RequestBody DepartmentRequest request,
            @PathVariable UUID id) {

        var department = departmentService.update(request, id);
        var response = departmentMapper.toResponse(department);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<DepartmentResponse>> findByName(@RequestParam("name") String name) {

        var department = departmentService.findByName(name);
        var response = departmentMapper.toResponse(department);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {

        departmentService.delete(id);
        var response = "Success";

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> findAll() {

        var departments = departmentService.findAll();
        var response = departments.stream().map(departmentMapper::toResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(response));
    }
}
