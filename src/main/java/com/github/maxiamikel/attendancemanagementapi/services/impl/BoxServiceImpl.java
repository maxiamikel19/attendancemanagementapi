package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.dto.request.BoxRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Box;
import com.github.maxiamikel.attendancemanagementapi.enums.BoxStatus;
import com.github.maxiamikel.attendancemanagementapi.exceptions.DuplicatedResourceException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ResourceNotFoundException;
import com.github.maxiamikel.attendancemanagementapi.repository.BoxRepository;
import com.github.maxiamikel.attendancemanagementapi.services.BoxService;
import com.github.maxiamikel.attendancemanagementapi.utils.ApiUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class BoxServiceImpl implements BoxService {

    private final BoxRepository boxRepository;

    @Override
    @Transactional
    public Box create(BoxRequest request) {

        String normalizerBoxNumber = ApiUtils.normalizeStringToUpperCase(request.getBoxNumber());

        validateBoxDoesNotExist(normalizerBoxNumber);

        Box box = buildBox(normalizerBoxNumber);

        log.info("Creating box with box number: {}", normalizerBoxNumber);

        return boxRepository.save(box);
    }

    @Override
    public Box findByBoxNumber(String boxNumber) {

        String normalizedBoxNumber = ApiUtils.normalizeStringToUpperCase(boxNumber);

        return boxRepository.findByBoxNumber(boxNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Box", normalizedBoxNumber));
    }

    @Override
    public Box update(BoxRequest request, UUID id) {
        Box box = getById(id);

        String boxNumber = ApiUtils.normalizeStringToUpperCase(request.getBoxNumber());

        if (boxRepository.existsByBoxNumberAndIdNot(boxNumber, id)) {
            throw new DuplicatedResourceException("Box", boxNumber);
        }

        log.info("Updating box {} to new name {}", box.getBoxNumber(), boxNumber);

        box.setBoxNumber(boxNumber);

        return boxRepository.save(box);

    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Box box = getById(id);
        boxRepository.delete(box);
    }

    private Box getById(UUID id) {
        return boxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Box", id.toString()));
    }

    private Box buildBox(String normalizerBoxNumber) {
        return Box.builder()
                .boxNumber(normalizerBoxNumber)
                .createdAt(LocalDateTime.now())
                .status(BoxStatus.AVAILABLE)
                .build();
    }

    private void validateBoxDoesNotExist(String boxNumber) {
        if (boxRepository.existsByBoxNumber(boxNumber)) {
            throw new DuplicatedResourceException("Box", boxNumber);
        }
    }
}
