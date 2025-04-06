package com.example.menuservice.service;

import com.example.menuservice.domain.Material;
import com.example.menuservice.dto.MaterialRequestDTO;
import com.example.menuservice.dto.MaterialResponseDTO;
import com.example.menuservice.exception.MaterialAlreadyExistsException;
import com.example.menuservice.exception.MaterialNotFoundException;
import com.example.menuservice.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final FileUploadService fileUploadService;

    // 재료 목록 조회
    public List<MaterialResponseDTO> viewMaterialList() {
        return materialRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 재료 이름으로 조회
    public MaterialResponseDTO viewMaterial(String materialName) {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));
        return toResponseDTO(material);
    }

    // 재료 추가
    @Transactional
    public MaterialResponseDTO addMaterial(MaterialRequestDTO requestDTO, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ✅ 1. 중복 체크
            if (materialRepository.existsByMaterialName(requestDTO.getMaterialName())) {
                throw new MaterialAlreadyExistsException(requestDTO.getMaterialName());
            }

            // ✅ 2. S3에 이미지 업로드 (DB 저장 전에 수행)
            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 3. 재료 정보 저장
            Material material = Material.builder()
                    .materialName(requestDTO.getMaterialName())
                    .calorie(requestDTO.getCalorie())
                    .price(requestDTO.getPrice())
                    .img(fileUrl)
                    .status("active")
                    .build();

            return toResponseDTO(materialRepository.save(material));

        } catch (Exception e) {
            // 🚨 트랜잭션 롤백 전에 업로드된 이미지 삭제
            if (fileUrl != null) {
                try {
                    fileUploadService.deleteFile(fileUrl);
                    System.out.println("🚨 저장 실패로 인해 S3 파일 삭제 완료: " + fileUrl);
                } catch (Exception s3Exception) {
                    System.out.println("⚠ S3 파일 삭제 실패: " + fileUrl);
                }
            }

            throw e; // 예외 다시 던져서 트랜잭션 롤백
        }
    }

    // 재료 삭제 (상태를 "DELETED"로 변경)
    @Transactional
    public void removeMaterial(String materialName) {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));

        // ✅ 상태를 "DELETED"로 변경
        material.setStatus("DELETED");

        // ✅ 변경된 상태 저장
        materialRepository.save(material);
    }

    // 재료 수정
    @Transactional
    public MaterialResponseDTO editMaterialDetails(String materialName, MaterialRequestDTO requestDTO, MultipartFile file) throws IOException {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));

        String fileUrl = material.getImg();

        try {
            // ✅ 새 이미지 업로드 시 기존 이미지 삭제
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 변경 감지로 자동 업데이트
            material.updateMaterial(
                    requestDTO.getMaterialName(),
                    requestDTO.getCalorie(),
                    requestDTO.getPrice(),
                    fileUrl // 새 이미지 URL 반영
            );

            return toResponseDTO(material);
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

    // 재료 상태 업데이트
    @Transactional
    public void updateMainMaterialStatus(Long uid, String status) {
        Material material = materialRepository.findById(uid)
                .orElseThrow(() -> new MaterialNotFoundException("ID: " + uid));
        material.setStatus(status);
    }

    // Material -> MaterialResponseDTO 변환 메서드
    private MaterialResponseDTO toResponseDTO(Material material) {
        return new MaterialResponseDTO(
                material.getUid(),
                material.getMaterialName(),
                material.getCalorie(),
                material.getPrice(),
                material.getImg(),
                material.getStatus(),
                material.getCreatedDate(),
                material.getVersion()
        );
    }
}
