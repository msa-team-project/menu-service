package com.example.menuservice.service;

import com.example.menuservice.status.SideStatus;
import com.example.menuservice.domain.Side;
import com.example.menuservice.dto.SideRequestDTO;
import com.example.menuservice.dto.SideResponseDTO;
import com.example.menuservice.exception.SideAlreadyExistsException;
import com.example.menuservice.exception.SideNotFoundException;
import com.example.menuservice.repository.SideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SideService {
    private final SideRepository sideRepository;
    private final FileUploadService fileUploadService;

    // 사이드 목록 조회
    public List<SideResponseDTO> viewSideList() {
        return sideRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 사이드 이름으로 사이드 조회
    public SideResponseDTO viewSide(String sideName) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));
        return toResponseDTO(side);
    }

    // 사이드 추가
    @Transactional
    public SideResponseDTO addSide(SideRequestDTO sideRequestDTO, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            if (sideRepository.existsBySideName(sideRequestDTO.getSideName())) {
                throw new SideAlreadyExistsException(sideRequestDTO.getSideName());
            }

            SideStatus status = SideStatus.valueOf(sideRequestDTO.getStatus().toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Side side = Side.builder()
                    .sideName(sideRequestDTO.getSideName())
                    .calorie(sideRequestDTO.getCalorie())
                    .price(sideRequestDTO.getPrice())
                    .status(status.name())
                    .img(fileUrl)
                    .build();

            return toResponseDTO(sideRepository.save(side));
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 사이드 삭제
    @Transactional
    public void removeSide(String sideName) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));
        side.setStatus(SideStatus.DELETED.name());
        sideRepository.save(side);
    }

    // 사이드 수정
    @Transactional
    public SideResponseDTO editSideDetails(String sideName, SideRequestDTO sideRequestDTO, MultipartFile file) throws IOException {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));

        String fileUrl = side.getImg();
        try {
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            side.updateSide(
                    sideRequestDTO.getSideName(),
                    sideRequestDTO.getCalorie(),
                    sideRequestDTO.getPrice(),
                    fileUrl,
                    sideRequestDTO.getStatus()
            );

            return toResponseDTO(side);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
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
                side.getImg(),
                side.getStatus(),
                side.getCreatedDate(),
                side.getVersion()
        );
    }
}
