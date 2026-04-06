package com.github.maxiamikel.attendancemanagementapi.mapper;

import org.springframework.stereotype.Component;

import com.github.maxiamikel.attendancemanagementapi.dto.response.BoxResponse;
import com.github.maxiamikel.attendancemanagementapi.entity.Box;

@Component
public class BoxMapper {

    public BoxResponse toResponse(Box entity) {
        return BoxResponse.builder()
                .id(entity.getId())
                .boxNumber(entity.getBoxNumber())
                .status(entity.getStatus().name())
                .build();
    }
}
