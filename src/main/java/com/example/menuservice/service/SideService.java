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

    // 사이드 이름으로 조회
    public SideResponseDTO viewSide(String sideName) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));
        return toResponseDTO(side);
    }

    // 사이드 추가
    @Transactional
    public SideResponseDTO addSide(SideRequestDTO requestDTO, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ✅ 1. 중복 체크
            if (sideRepository.existsBySideName(requestDTO.getSideName())) {
                throw new SideAlreadyExistsException(requestDTO.getSideName());
            }

            // ✅ 2. S3에 이미지 업로드 (DB 저장 전에 수행)
            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 3. 사이드 정보 저장
            Side side = Side.builder()
                    .sideName(requestDTO.getSideName())
                    .calorie(requestDTO.getCalorie())
                    .price(requestDTO.getPrice())
                    .img(fileUrl)
                    .status("active")
                    .build();

            return toResponseDTO(sideRepository.save(side));

        } catch (Exception e) {
            // 🚨 트랜잭션 롤백 전에 업로드된 이미지 삭제
            if (fileUrl != null) {
                try {
                    fileUploadService.deleteFile(fileUrl);
                } catch (Exception s3Exception) {
                    System.out.println("⚠ S3 파일 삭제 실패: " + fileUrl);
                }
            }

            throw e; // 예외 다시 던져서 트랜잭션 롤백
        }
    }

    // 사이드 삭제 (상태를 "DELETED"로 변경)
    @Transactional
    public void removeSide(String sideName) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));

        // ✅ 상태를 "DELETED"로 변경
        side.setStatus("DELETED");

        // ✅ 변경된 상태 저장
        sideRepository.save(side);
    }

    // 사이드 수정
    @Transactional
    public SideResponseDTO editSideDetails(String sideName, SideRequestDTO requestDTO, MultipartFile file) throws IOException {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));

        String fileUrl = side.getImg();

        try {
            // ✅ 새 이미지 업로드 시 기존 이미지 삭제
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 변경 감지로 자동 업데이트
            side.updateSide(
                    requestDTO.getSideName(),
                    requestDTO.getCalorie(),
                    requestDTO.getPrice(),
                    fileUrl // 새 이미지 URL 반영
            );

            return toResponseDTO(side);
        } catch (Exception e) {
            // 🚨 트랜잭션 롤백 전에 업로드된 새 이미지 삭제
            if (fileUrl != null) {
                try {
                    fileUploadService.deleteFile(fileUrl);
                } catch (Exception s3Exception) {
                    System.out.println("⚠ S3 파일 삭제 실패: " + fileUrl);
                }
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
                side.getStatus(),
                side.getImg(),
                side.getCreatedDate(),
                side.getVersion()
        );
    }
}
