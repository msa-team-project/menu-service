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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SideService {
    private final SideRepository sideRepository;

    // 사이드 목록 조회
    public List<SideResponseDTO> viewSideList() {
        List<SideResponseDTO> sideResponseDTOList = new ArrayList<>();
        for (Side side : sideRepository.findAll()) {
            sideResponseDTOList.add(toResponseDTO(side));
        }
        return sideResponseDTOList;
    }

    // 사이드 이름으로 사이드 조회
    public SideResponseDTO viewSide(String sideName) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));
        return toResponseDTO(side);
    }

    // 사이드 추가
    public SideResponseDTO addSide(SideRequestDTO sideRequestDTO) {
        if (sideRepository.existsBySideName(sideRequestDTO.getSideName())) {
            throw new SideAlreadyExistsException(sideRequestDTO.getSideName());
        }

        Side side = Side.builder()
                .sideName(sideRequestDTO.getSideName())
                .calorie(sideRequestDTO.getCalorie())
                .price(sideRequestDTO.getPrice())
                .img(sideRequestDTO.getImg())
                .status("active")
                .build();

        Side savedSide = sideRepository.save(side);
        return toResponseDTO(savedSide);
    }

    // 사이드 삭제
    public void removeSide(String sideName) {
        if (!sideRepository.existsBySideName(sideName)) {
            throw new SideNotFoundException(sideName);
        }
        sideRepository.deleteBySideName(sideName);
    }

    // 사이드 수정
    @Transactional
    public SideResponseDTO editSideDetails(String sideName, SideRequestDTO sideRequestDTO) {
        Side existingSide = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));

        Side updatedSide = Side.builder()
                .uid(existingSide.uid())
                .sideName(sideRequestDTO.getSideName())
                .calorie(sideRequestDTO.getCalorie())
                .price(sideRequestDTO.getPrice())
                .img(sideRequestDTO.getImg())
                .status(existingSide.status())
                .createdDate(existingSide.createdDate())
                .version(existingSide.version()) // 버전 증가
                .build();

        Side savedSide = sideRepository.save(updatedSide);
        return toResponseDTO(savedSide);
    }

    // 사이드 상태 업데이트
    public void updateSideStatus(Long uid, String status) {
        if (!sideRepository.existsById(uid)) {
            throw new SideNotFoundException("ID: " + uid);
        }
        sideRepository.updateStatusByUid(uid, status);
    }

    // Side -> SideResponseDTO 변환 메서드
    private SideResponseDTO toResponseDTO(Side side) {
        return new SideResponseDTO(
                side.uid(),
                side.sideName(),
                side.calorie(),
                side.price(),
                side.status(),
                side.img(),
                side.createdDate(),
                side.version()
        );
    }
}
