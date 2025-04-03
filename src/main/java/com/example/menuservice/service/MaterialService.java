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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;

    // 재료 목록 조회
    public List<MaterialResponseDTO> viewMainMaterialList() {
        return materialRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 재료 이름으로 조회
    public MaterialResponseDTO viewMainMaterial(String materialName) {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));
        return toResponseDTO(material);
    }

    // 재료 추가
    @Transactional
    public MaterialResponseDTO addMainMaterial(MaterialRequestDTO requestDTO) {
        if (materialRepository.existsByMaterialName(requestDTO.getMaterialName())) {
            throw new MaterialAlreadyExistsException(requestDTO.getMaterialName());
        }

        Material material = Material.builder()
                .materialName(requestDTO.getMaterialName())
                .calorie(requestDTO.getCalorie())
                .price(requestDTO.getPrice())
                .img(requestDTO.getImg())
                .status("active")
                .build();

        return toResponseDTO(materialRepository.save(material));
    }

    // 재료 삭제
    @Transactional
    public void removeMainMaterial(String materialName) {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));
        materialRepository.delete(material);
    }

    // 재료 수정 (변경 감지 기능 활용)
    @Transactional
    public MaterialResponseDTO editMainMaterialDetails(String materialName, MaterialRequestDTO requestDTO) {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));

        // ✅ 변경 감지를 활용해 자동 업데이트
        material.updateMaterial(
                requestDTO.getMaterialName(),
                requestDTO.getCalorie(),
                requestDTO.getPrice(),
                requestDTO.getImg()
        );

        return toResponseDTO(material);
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
