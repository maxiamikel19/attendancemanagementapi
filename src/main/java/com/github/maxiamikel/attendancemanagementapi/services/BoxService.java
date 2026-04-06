package com.github.maxiamikel.attendancemanagementapi.services;

import java.util.UUID;

import com.github.maxiamikel.attendancemanagementapi.dto.request.BoxRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Box;

public interface BoxService {

    Box create(BoxRequest request);

    Box findByBoxNumber(String boxNumber);

    Box update(BoxRequest request, UUID id);

    void delete(UUID id);
}
