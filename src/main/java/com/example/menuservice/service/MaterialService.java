package com.example.menuservice.service;

import com.example.menuservice.domain.Material;
import com.example.menuservice.dto.MaterialRequestDTO;
import com.example.menuservice.dto.MaterialResponseDTO;
import com.example.menuservice.exception.MaterialAlreadyExistsException;
import com.example.menuservice.exception.MaterialNotFoundException;
import com.example.menuservice.repository.MaterialRepository;
import com.example.menuservice.status.MaterialStatus;
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

    // 재료 이름으로 재료 조회
    public MaterialResponseDTO viewMaterial(String materialName) {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));
        return toResponseDTO(material);
    }

    // 재료 추가
    @Transactional
    public MaterialResponseDTO addMaterial(MaterialRequestDTO materialRequestDTO, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            if (materialRepository.existsByMaterialName(materialRequestDTO.getMaterialName())) {
                throw new MaterialAlreadyExistsException(materialRequestDTO.getMaterialName());
            }

            MaterialStatus status = MaterialStatus.valueOf(materialRequestDTO.getStatus().toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Material material = Material.builder()
                    .materialName(materialRequestDTO.getMaterialName())
                    .calorie(materialRequestDTO.getCalorie())
                    .price(materialRequestDTO.getPrice())
                    .img(fileUrl)
                    .status(status.name())
                    .build();

            return toResponseDTO(materialRepository.save(material));
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 재료 삭제
    @Transactional
    public void removeMaterial(String materialName) {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));
        material.setStatus(MaterialStatus.DELETED.name());
        materialRepository.save(material);
    }

    // 재료 수정
    @Transactional
    public MaterialResponseDTO editMaterialDetails(String materialName, MaterialRequestDTO materialRequestDTO, MultipartFile file) throws IOException {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));

        String fileUrl = material.getImg();

        try {
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            material.updateMaterial(
                    materialRequestDTO.getMaterialName(),
                    materialRequestDTO.getCalorie(),
                    materialRequestDTO.getPrice(),
                    fileUrl,
                    materialRequestDTO.getStatus()
            );

            return toResponseDTO(material);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 재료 상태 업데이트
    @Transactional
    public void updateMaterialStatus(Long uid, String status) {
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
