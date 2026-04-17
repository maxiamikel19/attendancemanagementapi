package com.github.maxiamikel.attendancemanagementapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.AttendanceDashboardResponse;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.services.AttendanceDashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/dashboards")
@RequiredArgsConstructor
@Tag(name = "Public Dashboard", description = "API pública para visualizar en tiempo real el estado de atención de tickets (pantallas en sala de espera)")
public class AttendanceDashboardController {

    private final AttendanceDashboardService dashboardService;

    @Operation(summary = "Obtener dashboard en tiempo real", description = """
                Retorna el estado actual del sistema de atención:

                - Tickets en espera (WAITING)
                - Ticket actualmente en atención (CURRENT)
                - Últimos tickets llamados (LAST CALLED)

                Este endpoint es público y está diseñado para pantallas de visualización en sala.
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dashboard obtenido correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<AttendanceDashboardResponse>> getDashboard() {

        var dashboard = dashboardService.getDashboard();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(dashboard));
    }

}
