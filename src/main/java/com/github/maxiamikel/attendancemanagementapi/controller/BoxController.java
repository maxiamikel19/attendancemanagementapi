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
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.request.BoxRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.BoxResponse;
import com.github.maxiamikel.attendancemanagementapi.entity.Box;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.BoxMapper;
import com.github.maxiamikel.attendancemanagementapi.services.BoxService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/boxes")
@RequiredArgsConstructor
@Tag(name = "Boxes", description = "Gestión de boxs o cajas o puntos de atención")
public class BoxController {

    private final BoxService boxService;
    private final BoxMapper boxMapper;

    @Operation(summary = "Crear una nueva caja o box o punto")
    @ApiResponses(value = {

            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Caja creada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<BoxResponse>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del box", required = true) @Valid @RequestBody BoxRequest request) {

        var box = boxService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.created(boxMapper.toResponse(box)));
    }

    @Operation(summary = "Buscar box por número")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Box encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Box no encontrado")
    })
    @GetMapping("/{boxNumber}")
    public ResponseEntity<ApiResponse<BoxResponse>> findByBoxNumber(
            @Parameter(description = "Número del box", required = true, example = "B-02") @PathVariable String boxNumber) {

        return ok(boxService.findByBoxNumber(boxNumber));
    }

    @Operation(summary = "Listar todos los boxes")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Boxex encontrados o lista vacia")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoxResponse>>> findAll() {

        var boxes = boxService.findAll()
                .stream()
                .map(boxMapper::toResponse)
                .toList();
        return ResponseEntity.ok(
                ApiResponseFactory.success(boxes));
    }

    @Operation(summary = "Actualizar un box")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "20o", description = "Box actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Box no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Datos duplicados")

    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BoxResponse>> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de actualización del box", required = true) @Valid @RequestBody BoxRequest request,
            @Parameter(description = "ID del box", required = true) @PathVariable UUID id) {

        return ok(boxService.update(request, id));
    }

    @Operation(summary = "Eliminar un box")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Box eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Box no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {

        boxService.delete(id);

        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<ApiResponse<BoxResponse>> ok(Box box) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(toResponse(box)));

    }

    private BoxResponse toResponse(Box box) {
        return boxMapper.toResponse(box);
    }
}
