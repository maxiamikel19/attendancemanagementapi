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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/boxes")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;
    private final BoxMapper boxMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<BoxResponse>> create(@Valid @RequestBody BoxRequest request) {

        var box = boxService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.created(boxMapper.toResponse(box)));
    }

    @GetMapping("/{boxNumber}")
    public ResponseEntity<ApiResponse<BoxResponse>> findByBoxNumber(@PathVariable String boxNumber) {

        return ok(boxService.findByBoxNumber(boxNumber));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BoxResponse>>> findAll() {

        var boxes = boxService.findAll()
                .stream()
                .map(boxMapper::toResponse)
                .toList();
        return ResponseEntity.ok(
                ApiResponseFactory.success(boxes));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BoxResponse>> update(@Valid @RequestBody BoxRequest request,
            @PathVariable UUID id) {

        return ok(boxService.update(request, id));
    }

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
