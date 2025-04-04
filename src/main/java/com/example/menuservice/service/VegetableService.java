package com.example.menuservice.service;

import com.example.menuservice.domain.Vegetable;
import com.example.menuservice.dto.VegetableRequestDTO;
import com.example.menuservice.dto.VegetableResponseDTO;
import com.example.menuservice.exception.VegetableAlreadyExistsException;
import com.example.menuservice.exception.VegetableNotFoundException;
import com.example.menuservice.repository.VegetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VegetableService {
    private final VegetableRepository vegetableRepository;
    private final FileUploadService fileUploadService;

    // 채소 목록 조회
    public List<VegetableResponseDTO> viewVegetableList() {
        return vegetableRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 채소 이름으로 조회
    public VegetableResponseDTO viewVegetable(String vegetableName) {
        Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));
        return toResponseDTO(vegetable);
    }

    // 채소 추가
    @Transactional
    public VegetableResponseDTO addVegetable(VegetableRequestDTO requestDTO, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ✅ 1. 중복 체크
            if (vegetableRepository.existsByVegetableName(requestDTO.getVegetableName())) {
                throw new VegetableAlreadyExistsException(requestDTO.getVegetableName());
            }

            // ✅ 2. S3에 이미지 업로드 (DB 저장 전에 수행)
            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 3. 채소 정보 저장
            Vegetable vegetable = Vegetable.builder()
                    .vegetableName(requestDTO.getVegetableName())
                    .calorie(requestDTO.getCalorie())
                    .price(requestDTO.getPrice())
                    .img(fileUrl)
                    .status("active")
                    .build();

            return toResponseDTO(vegetableRepository.save(vegetable));

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

    // 채소 삭제 (상태를 "DELETED"로 변경)
    @Transactional
    public void removeVegetable(String vegetableName) {
        Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));

        // ✅ 상태를 "DELETED"로 변경
        vegetable.setStatus("DELETED");

        // ✅ 변경된 상태 저장
        vegetableRepository.save(vegetable);
    }

    // 채소 수정
    @Transactional
    public VegetableResponseDTO editVegetableDetails(String vegetableName, VegetableRequestDTO requestDTO, MultipartFile file) throws IOException {
        Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));

        String fileUrl = vegetable.getImg();

        try {
            // ✅ 새 이미지 업로드 시 기존 이미지 삭제
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 변경 감지로 자동 업데이트
            vegetable.updateVegetable(
                    requestDTO.getVegetableName(),
                    requestDTO.getCalorie(),
                    requestDTO.getPrice(),
                    fileUrl // 새 이미지 URL 반영
            );

            return toResponseDTO(vegetable);
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

    // 채소 상태 업데이트
    @Transactional
    public void updateVegetableStatus(Long uid, String status) {
        Vegetable vegetable = vegetableRepository.findById(uid)
                .orElseThrow(() -> new VegetableNotFoundException("ID: " + uid));
        vegetable.setStatus(status);
    }

    // Vegetable -> VegetableResponseDTO 변환 메서드
    private VegetableResponseDTO toResponseDTO(Vegetable vegetable) {
        return new VegetableResponseDTO(
                vegetable.getUid(),
                vegetable.getVegetableName(),
                vegetable.getCalorie(),
                vegetable.getPrice(),
                vegetable.getImg(),
                vegetable.getStatus(),
                vegetable.getCreatedDate(),
                vegetable.getVersion()
        );
    }
}
