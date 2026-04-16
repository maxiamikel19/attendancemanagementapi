package com.github.maxiamikel.attendancemanagementapi.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Detalles de Ticket")
public class TicketDetailsResponse {

    @Schema(description = "Id del ticket ")
    private UUID id;

    @Schema(description = "Pass-Code del ticket ", example = "TI-001")
    private String passCode;

    @Schema(description = "Box de atención del ticket ", example = "B-02")
    private String box;

    @Schema(description = "Departamento (Sector) de atención del ticket ", example = "EXECUTIVE")
    private String department;

    @Schema(description = "Numero de identificación personal del cliente", example = "***.001.938-**")
    private String personalId;

    @Schema(description = "Estado de la atención", example = "CALLED")
    private String ticketStatus;

    @Schema(description = "Prioridad de la atención ", example = "NORMAL")
    private String priority;

    @Schema(description = "fecha y hora de emición del ticket ")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora del último atención del ticket ")
    private LocalDateTime lastUpdate;

    @Schema(description = "Cantidad de veces que es llamado por su atención ")
    private int recallCount;
}
