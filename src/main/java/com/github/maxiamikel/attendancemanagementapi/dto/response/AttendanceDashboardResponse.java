package com.github.maxiamikel.attendancemanagementapi.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "AttendanceDashboardResponse", description = "Representa el estado actual del sistema de atención para visualización en tiempo real (pantallas públicas)")
public class AttendanceDashboardResponse {

    @Schema(description = "Lista de tickets en estado WAITING (en espera de ser llamados)", example = "[{ \"passCode\": \"A001\", \"priority\": \"NORMAL\" }]")
    private List<TicketDetailsResponse> waiting;

    @Schema(description = "Lista de los últimos tickets llamados (historial reciente mostrado en pantalla)", example = "[{ \"passCode\": \"A010\" }, { \"passCode\": \"A011\" }]")
    private List<TicketDetailsResponse> recentCalled;

    @Schema(description = "Ticket actualmente en atención", example = "{ \"passCode\": \"A012\", \"box\": \"Box 3\" }")
    private TicketDetailsResponse current;
}
