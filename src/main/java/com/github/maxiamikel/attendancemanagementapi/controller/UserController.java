package com.github.maxiamikel.attendancemanagementapi.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.request.UserRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.UserResponse;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.UserMapper;
import com.github.maxiamikel.attendancemanagementapi.security.CustomUserDetails;
import com.github.maxiamikel.attendancemanagementapi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Gestión de usuarios del sistema")
public class UserController {

        private final UserService userService;
        private final UserMapper userMapper;

        @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema con sus datos básicos.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Usuario ya existe"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno")
        })
        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest request) {

                var user = userService.createUser(request);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ApiResponseFactory.created(userMapper.toResponse(user)));
        }

        @Operation(summary = "Obtener usuario autenticado", description = "Retorna la información del usuario actualmente autenticado.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado")
        })
        @GetMapping("/me")
        public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
                        @AuthenticationPrincipal CustomUserDetails user) {

                return ok(userService.findById(user.getId()));
        }

        @Operation(summary = "Obtener usuario por ID")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })
        @GetMapping("/{userId}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID userId) {

                return ok(userService.findById(userId));
        }

        @Operation(summary = "Listar usuarios")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de usuarios"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })
        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<?>> getAll() {

                var users = userService.findAll();
                return ResponseEntity
                                .ok(ApiResponseFactory.success(users.stream().map(userMapper::toResponse).toList()));
        }

        @Operation(summary = "Asignar caja o box a usuario")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Caja asignada"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario o caja no encontrada"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Usuario yá tiene un box asignnado o el box no esta disponible a ser asignado")
        })
        @PutMapping("/{userId}/box/{boxId}")
        public ResponseEntity<ApiResponse<UserResponse>> addBox(@PathVariable UUID userId, @PathVariable UUID boxId) {

                return ok(userService.addBox(userId, boxId));
        }

        @Operation(summary = "Actualizar caja del usuario")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Caja asignada"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Box o cala está en uso de otro usuario"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario o caja no encontrada"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del sistema")
        })
        @PutMapping("/{userId}/box/{boxId}/update")
        public ResponseEntity<ApiResponse<UserResponse>> updateBox(@PathVariable UUID userId,
                        @PathVariable UUID boxId) {

                return ok(userService.updateBox(userId, boxId));
        }

        @Operation(summary = "Remover caja del usuario")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Caja removida"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        @PutMapping("/{userId}/box")
        public ResponseEntity<ApiResponse<UserResponse>> removeBox(@PathVariable("userId") UUID userId) {

                return ok(userService.removeBox(userId));
        }

        @Operation(summary = "Eliminar usuario")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Usuario eliminado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        @DeleteMapping("/{userId}")
        public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable("userId") UUID userId) {

                userService.deleteUser(userId);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Actualizar rol del usuario")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rol actualizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rol o usuario no encontrado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })
        @PutMapping("/{userId}/role/{roleId}")
        public ResponseEntity<ApiResponse<UserResponse>> updateRole(@PathVariable UUID userId,
                        @PathVariable UUID roleId) {

                return ok(userService.updateRole(userId, roleId));
        }

        @Operation(summary = "Cambiar departamento del usuario")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departamento actualizado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario o departamento no encontrado")
        })
        @PutMapping("/{userId}/department/{departmentId}")
        public ResponseEntity<ApiResponse<UserResponse>> changeDepartment(@PathVariable UUID userId,
                        @PathVariable UUID departmentId) {

                return ok(userService.updateDepartment(userId, departmentId));
        }

        private ResponseEntity<ApiResponse<UserResponse>> ok(User user) {
                return ResponseEntity.ok(
                                ApiResponseFactory.success(toResponse(user)));
        }

        private UserResponse toResponse(User user) {
                return userMapper.toResponse(user);
        }

}
