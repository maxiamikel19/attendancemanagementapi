package com.github.maxiamikel.attendancemanagementapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.request.TicketRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.TicketResponse;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.TicketMapper;
import com.github.maxiamikel.attendancemanagementapi.services.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Gestión de turnos y tickets de atención")
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Operation(summary = "Generar un nuevo ticket", description = """
                Genera un nuevo ticket de atención basado en la información proporcionada.
                El ticket será asignado a un departamento o flujo de atención
                y quedará en estado 'WAITING' hasta ser llamado 'CALLED'.
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Ticket generado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud o Usuario yá tiene un ticket activo para el departamento"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Departamento o recurso no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> generateTicket(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para generar el ticket", required = true) @Valid @RequestBody TicketRequest request) {
        var ticket = ticketService.generateTicket(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.created(ticketMapper.toResponse(ticket)));
    }

}
