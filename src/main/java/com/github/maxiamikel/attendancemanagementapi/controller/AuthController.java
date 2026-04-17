package com.github.maxiamikel.attendancemanagementapi.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.request.AuthRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.AccessToken;
import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.MessageResponse;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Gestión de autenticación y activación de cuentas de usuario")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Activar cuenta de usuario", description = """
                Activa una cuenta de usuario mediante su identificador único.
                Este endpoint normalmente es consumido desde un link enviado por email.
            """)
    @ApiResponses(value = {

            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cuenta activada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "ID inválido o cuenta ya activada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/activate-account")
    public ResponseEntity<ApiResponse<MessageResponse>> activateNewAccount(
            @Parameter(description = "ID único del usuario a activar", required = true) @RequestParam("id") UUID id) {

        authService.activateNewAccount(id);
        MessageResponse response = MessageResponse
                .builder()
                .message("Your account has been successfully activated")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(response));
    }

    @Operation(summary = "Autenticación de usuario", description = """
                Permite a un usuario autenticarse con sus credenciales.
                Retorna un access token (JWT) que debe ser utilizado en los endpoints protegidos.
            """)
    @ApiResponses(value = {

            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AccessToken>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciales de login", required = true) @Valid @RequestBody AuthRequest request) {

        var response = authService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(response));
    }
}
