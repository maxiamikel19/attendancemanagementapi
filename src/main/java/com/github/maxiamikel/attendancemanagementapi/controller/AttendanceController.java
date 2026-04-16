package com.github.maxiamikel.attendancemanagementapi.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
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

        @Operation(summary = "Llamar al siguiente ticket", description = "Obtiene el siguiente ticket disponible segun su prioridad (PRIORITY o NORMAL) y lo marca como CALLED")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no authenticado"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No hay ticket dispinoble"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Tiene que finilize o atendimiento en curso antes de atender a otro")
        })
        @PostMapping("/next")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> callNext(
                        @AuthenticationPrincipal CustomUserDetails user) {
                var nextTicket = attendanceService.callNextTicket(getUserId(user));

                return ok(nextTicket);
        }

        @Operation(summary = "Llamar al siguiente ticket", description = "Obtiene el siguiente ticket disponible segun PRIORITY o NORMAL definido por el usuario y lo marca como CALLED")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no authenticado "),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No hay ticket disponible "),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Tiene que finilize o atendimiento en curso antes de atender a otro")
        })
        @PostMapping("/next-by-priority")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> callNextTicketByPriority(
                        @Parameter(description = "Priodidad del Ticket", example = "PRIORITY") @RequestParam TicketPriority priority,
                        @AuthenticationPrincipal CustomUserDetails user) {
                var nextTicket = attendanceService.callNextTicketByPriority(priority, getUserId(user));

                return ok(nextTicket);
        }

        @Operation(summary = "Re-Llamar a un ticket llamado", description = "Llamar nuevamente a un ticket que fue lladado(CALLED) hasta 4 veces y lo marca como CANCELLED")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK. Devuelve el ticket llamado y incrementa su recallcount+=1"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No encontrar ticket con TicketStatus CALLED"),
        })
        @PostMapping("/recall")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> recallTicket(
                        @AuthenticationPrincipal CustomUserDetails user) {
                var waitted = attendanceService.recallTicket(getUserId(user));

                return ok(waitted);
        }

        @Operation(summary = "Iniciar una atención", description = "Iniciar la atención de un ticket CALLED y lo cambia el TicketStatus a ATTENDING")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK. Devuelve el ticket ATTENDING"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No encontrar ticket con TicketStatus CALLED"),
        })
        @PostMapping("/start")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> startTicket(
                        @AuthenticationPrincipal CustomUserDetails user) {
                var called = attendanceService.startTicket(getUserId(user));

                return ok(called);
        }

        @Operation(summary = "Terminar una atención", description = "Terminar la atención de un ticket ATTENDING y lo cambia el TicketStatus a FINALIZED")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK. Devuelve el ticket FINALIZED"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No encontrar ticket con TicketStatus ATTENDING"),
        })
        @PostMapping("/finalize")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> finalizeTicket(
                        @AuthenticationPrincipal CustomUserDetails user) {
                var attended = attendanceService.finalizeTicket(getUserId(user));

                return ok(attended);
        }

        @Operation(summary = "Encontrar la actual atencion", description = "Devuelve el actual ticket en atencion,sea con Ticketstatus CALLED, ATTENDING")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK. Devuelve el ticket ATTENDING"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No encontrar ningun ticket activo sea CALLED o ATTENDING"),
        })
        @GetMapping("/current")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> getCurrentTicketTicket(
                        @AuthenticationPrincipal CustomUserDetails user) {
                var attended = attendanceService.getCurrentTicketTicket(getUserId(user));

                return ok(attended);
        }

        @Operation(summary = "Transferir una atención a otro departamento", description = "Hace la transferencia de un ticket generado para un Department equivocado al su correcto Department para su atención")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK. Devuelve el ticket con TicketStatus ATTENDING a WAITING y cambia el departamento para su atención "),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No encontrar ticket activo con TicketStatus ATTENDING"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "No se poede transferir una atención al mismo department")
        })
        @PatchMapping("/transfer/{departmentId}")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> transferTicket(
                        @Parameter(description = "departmentId", example = "0fc357c3-c548-430c-af20-65dd9febff34") @AuthenticationPrincipal CustomUserDetails user,
                        @PathVariable UUID departmentId) {
                var attended = attendanceService.transferTicket(departmentId, getUserId(user));

                return ok(attended);
        }

        @Operation(summary = "Lista la fila de espera por department", description = "Listar los Tickets en espera (WAITING) del departemento al que pertenece el usuario logado")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK.Devuelve la lista vacía o con las atenciones en espere (WAITING) ")
        })
        @GetMapping("/tickets")
        public ResponseEntity<ApiResponse<List<TicketDetailsResponse>>> getAllWaitingAttendencesForDepartment(
                        @AuthenticationPrincipal CustomUserDetails user) {

                var tickets = attendanceService.getTicketsByStatus(getUserId(user));

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(ticketMapper.toDetailsList(tickets)));
        }

        private UUID getUserId(CustomUserDetails user) {
                if (user == null) {
                        throw new CredentialException("User not authenticated");
                }
                return user.getId();
        }

        private ResponseEntity<ApiResponse<TicketDetailsResponse>> ok(Ticket ticket) {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(toResponse(ticket)));
        }

        private TicketDetailsResponse toResponse(Ticket ticket) {
                return ticketMapper.toDetailsResponse(ticket);
        }

}
