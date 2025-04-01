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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;

    // 재료 목록 조회
    public List<MaterialResponseDTO> viewMainMaterialList() {
        List<MaterialResponseDTO> responseDTOList = new ArrayList<>();
        for (Material material : materialRepository.findAll()) {
            responseDTOList.add(toResponseDTO(material));
        }
        return responseDTOList;
    }

    // 재료 이름으로 조회
    public MaterialResponseDTO viewMainMaterial(String materialName) {
        Material material = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));
        return toResponseDTO(material);
    }

    // 재료 추가
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
    public void removeMainMaterial(String materialName) {
        if (!materialRepository.existsByMaterialName(materialName)) {
            throw new MaterialNotFoundException(materialName);
        }
        materialRepository.deleteByMaterialName(materialName);
    }

    // 재료 수정
    @Transactional
    public MaterialResponseDTO editMainMaterialDetails(String materialName, MaterialRequestDTO requestDTO) {
        Material existingMaterial = materialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MaterialNotFoundException(materialName));

        Material updatedMaterial = Material.builder()
                .uid(existingMaterial.uid())
                .materialName(requestDTO.getMaterialName())
                .calorie(requestDTO.getCalorie())
                .price(requestDTO.getPrice())
                .img(requestDTO.getImg())
                .status(existingMaterial.status())
                .createdDate(existingMaterial.createdDate())
                .version(existingMaterial.version()) // 버전 증가
                .build();

        return toResponseDTO(materialRepository.save(updatedMaterial));
    }

    // 재료 상태 업데이트
    public void updateMainMaterialStatus(Long uid, String status) {
        if (!materialRepository.existsById(uid)) {
            throw new MaterialNotFoundException("ID: " + uid);
        }
        materialRepository.updateStatusByUid(uid, status);
    }

    // MainMaterial -> MainMaterialResponseDTO 변환 메서드
    private MaterialResponseDTO toResponseDTO(Material material) {
        return new MaterialResponseDTO(
                material.uid(),
                material.materialName(),
                material.calorie(),
                material.price(),
                material.img(),
                material.status(),
                material.createdDate(),
                material.version()
        );
    }
}
