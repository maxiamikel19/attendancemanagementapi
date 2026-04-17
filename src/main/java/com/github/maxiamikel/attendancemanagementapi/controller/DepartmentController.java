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
import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.DepartmentMapper;
import com.github.maxiamikel.attendancemanagementapi.services.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "Gestión de departamentos")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @Operation(summary = "Crear un departamento", description = "Crea un nuevo departamento en el sistema")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Departamento creado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflicto de duplicado")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> create(@Valid @RequestBody DepartmentRequest request) {

        var department = departmentService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.created(departmentMapper.toResponse(department)));
    }

    @Operation(summary = "Actualizar un departamento", description = "Actualiza un departamento existente por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Departamento actualizado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Departamento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflicto de duplicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> update(@Valid @RequestBody DepartmentRequest request,
            @PathVariable UUID id) {

        return ok(departmentService.update(request, id));
    }

    @Operation(summary = "Buscar departamento por nombre", description = "Obtiene un departamento basado en su nombre")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departamento encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    })
    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<DepartmentResponse>> findByName(@RequestParam("name") String name) {

        return ok(departmentService.findByName(name));
    }

    @Operation(summary = "Eliminar un departamento", description = "Elimina un departamento por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Departamento creado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Departamento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Departamento no se puede eliminar al tener alguno usuario relacionado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {

        departmentService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos los departamentos", description = "Obtiene una lista de todos los departamentos")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listar los departamentos del sistema o la lista vacía")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> findAll() {

        var departments = departmentService.findAll();
        var response = departments.stream().map(departmentMapper::toResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(response));
    }

    private ResponseEntity<ApiResponse<DepartmentResponse>> ok(Department department) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(toResponse(department)));
    }

    private DepartmentResponse toResponse(Department department) {
        return departmentMapper.toResponse(department);
    }
}
