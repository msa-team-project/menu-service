package com.example.menuservice.service;

import com.example.menuservice.domain.Side;
import com.example.menuservice.dto.SideRequestDTO;
import com.example.menuservice.dto.SideResponseDTO;
import com.example.menuservice.exception.SideAlreadyExistsException;
import com.example.menuservice.exception.SideNotFoundException;
import com.example.menuservice.repository.SideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SideService {
    private final SideRepository sideRepository;

    // 사이드 목록 조회
    public List<SideResponseDTO> viewSideList() {
        return sideRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 사이드 이름으로 조회
    public SideResponseDTO viewSide(String sideName) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));
        return toResponseDTO(side);
    }

    // 사이드 추가
    @Transactional
    public SideResponseDTO addSide(SideRequestDTO requestDTO) {
        if (sideRepository.existsBySideName(requestDTO.getSideName())) {
            throw new SideAlreadyExistsException(requestDTO.getSideName());
        }

        Side side = Side.builder()
                .sideName(requestDTO.getSideName())
                .calorie(requestDTO.getCalorie())
                .price(requestDTO.getPrice())
                .img(requestDTO.getImg())
                .status("active")
                .build();

        return toResponseDTO(sideRepository.save(side));
    }

    // 사이드 삭제
    @Transactional
    public void removeSide(String sideName) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));
        sideRepository.delete(side);
    }

    // 사이드 수정 (변경 감지 활용)
    @Transactional
    public SideResponseDTO editSideDetails(String sideName, SideRequestDTO requestDTO) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));

        // ✅ 변경 감지를 활용한 업데이트
        side.updateSide(
                requestDTO.getSideName(),
                requestDTO.getCalorie(),
                requestDTO.getPrice(),
                requestDTO.getImg()
        );

        return toResponseDTO(side);
    }

    // 사이드 상태 업데이트
    @Transactional
    public void updateSideStatus(Long uid, String status) {
        Side side = sideRepository.findById(uid)
                .orElseThrow(() -> new SideNotFoundException("ID: " + uid));
        side.setStatus(status);
    }

    // Side -> SideResponseDTO 변환 메서드
    private SideResponseDTO toResponseDTO(Side side) {
        return new SideResponseDTO(
                side.getUid(),
                side.getSideName(),
                side.getCalorie(),
                side.getPrice(),
                side.getStatus(),
                side.getImg(),
                side.getCreatedDate(),
                side.getVersion()
        );
    }
}
