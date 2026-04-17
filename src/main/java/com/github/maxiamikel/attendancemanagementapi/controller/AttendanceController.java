package com.github.maxiamikel.attendancemanagementapi.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.TicketDetailsResponse;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketPriority;
import com.github.maxiamikel.attendancemanagementapi.exceptions.CredentialException;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.TicketMapper;
import com.github.maxiamikel.attendancemanagementapi.security.CustomUserDetails;
import com.github.maxiamikel.attendancemanagementapi.services.AttendanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/attendances")
@RequiredArgsConstructor
@Tag(name = "Attendances", description = "Gestión de atención de tickets")
public class AttendanceController {

        private final AttendanceService attendanceService;
        private final TicketMapper ticketMapper;

        @Operation(summary = "Llamar siguiente ticket", description = "Obtiene el siguiente ticket disponible (por prioridad automática) y lo marca como CALLED")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ticket llamado correctamente"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No hay tickets disponibles"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe una atención en curso")
        })
        @PostMapping("/next")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> callNext(
                        @AuthenticationPrincipal CustomUserDetails user) {

                return ok(attendanceService.callNextTicket(getUserId(user)));
        }

        @Operation(summary = "Llamar siguiente ticket por prioridad", description = "Obtiene el siguiente ticket filtrado por prioridad (PRIORITY o NORMAL)")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ticket llamado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Prioridad inválida"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No hay tickets disponibles")
        })
        @PostMapping("/next-by-priority")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> callNextByPriority(
                        @Parameter(description = "Prioridad del ticket", example = "PRIORITY") @RequestParam TicketPriority priority,
                        @AuthenticationPrincipal CustomUserDetails user) {

                return ok(attendanceService.callNextTicketByPriority(priority, getUserId(user)));
        }

        @Operation(summary = "Re-llamar ticket", description = "Re-llama un ticket en estado CALLED. Máximo 4 intentos antes de cancelarlo automáticamente.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ticket re-llamado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No existe ticket en estado CALLED"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Límite de re-llamadas alcanzado")
        })
        @PostMapping("/recall")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> recall(
                        @AuthenticationPrincipal CustomUserDetails user) {

                return ok(attendanceService.recallTicket(getUserId(user)));
        }

        @Operation(summary = "Iniciar atención", description = "Cambia el ticket de CALLED a ATTENDING")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Atención iniciada"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No hay ticket en estado CALLED"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Estado inválido")
        })
        @PostMapping("/start")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> start(
                        @AuthenticationPrincipal CustomUserDetails user) {

                return ok(attendanceService.startTicket(getUserId(user)));
        }

        @Operation(summary = "Finalizar atención", description = "Cambia el ticket de ATTENDING a FINALIZED")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Atención finalizada"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No hay ticket en atención"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Estado inválido")
        })
        @PostMapping("/finalize")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> finalizeTicket(
                        @AuthenticationPrincipal CustomUserDetails user) {

                return ok(attendanceService.finalizeTicket(getUserId(user)));
        }

        @Operation(summary = "Obtener ticket actual", description = "Devuelve el ticket actual en estado CALLED o ATTENDING")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ticket actual"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No hay ticket activo")
        })
        @GetMapping("/current")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> current(
                        @AuthenticationPrincipal CustomUserDetails user) {

                return ok(attendanceService.getCurrentTicketTicket(getUserId(user)));
        }

        @Operation(summary = "Transferir ticket", description = "Transfiere un ticket en atención a otro departamento")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ticket transferido"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Departamento inválido"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "No se puede transferir al mismo departamento")
        })
        @PostMapping("/transfer/{departmentId}")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> transfer(
                        @PathVariable UUID departmentId,
                        @AuthenticationPrincipal CustomUserDetails user) {

                return ok(attendanceService.transferTicket(departmentId, getUserId(user)));
        }

        @Operation(summary = "Listar tickets en espera", description = "Lista los tickets en estado WAITING del departamento del usuario")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de tickets")
        })
        @GetMapping("/waiting")
        public ResponseEntity<ApiResponse<List<TicketDetailsResponse>>> waiting(
                        @AuthenticationPrincipal CustomUserDetails user) {

                var tickets = attendanceService.getTicketsByStatus(getUserId(user));

                return ResponseEntity.ok(
                                ApiResponseFactory.success(ticketMapper.toDetailsList(tickets)));
        }

        private UUID getUserId(CustomUserDetails user) {
                if (user == null) {
                        throw new CredentialException("User not authenticated");
                }
                return user.getId();
        }

        private ResponseEntity<ApiResponse<TicketDetailsResponse>> ok(Ticket ticket) {
                return ResponseEntity.ok(
                                ApiResponseFactory.success(ticketMapper.toDetailsResponse(ticket)));
        }
}
