package com.example.menuservice.service;

import com.example.menuservice.domain.MainMaterial;
import com.example.menuservice.dto.MainMaterialRequestDTO;
import com.example.menuservice.dto.MainMaterialResponseDTO;
import com.example.menuservice.exception.MainMaterialAlreadyExistsException;
import com.example.menuservice.exception.MainMaterialNotFoundException;
import com.example.menuservice.repository.MainMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainMaterialService {
    private final MainMaterialRepository mainMaterialRepository;

    // 재료 목록 조회
    public List<MainMaterialResponseDTO> viewMainMaterialList() {
        List<MainMaterialResponseDTO> responseDTOList = new ArrayList<>();
        for (MainMaterial mainMaterial : mainMaterialRepository.findAll()) {
            responseDTOList.add(toResponseDTO(mainMaterial));
        }
        return responseDTOList;
    }

    // 재료 이름으로 조회
    public MainMaterialResponseDTO viewMainMaterial(String materialName) {
        MainMaterial mainMaterial = mainMaterialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MainMaterialNotFoundException(materialName));
        return toResponseDTO(mainMaterial);
    }

    // 재료 추가
    public MainMaterialResponseDTO addMainMaterial(MainMaterialRequestDTO requestDTO) {
        if (mainMaterialRepository.existsByMaterialName(requestDTO.getMaterialName())) {
            throw new MainMaterialAlreadyExistsException(requestDTO.getMaterialName());
        }

        MainMaterial mainMaterial = MainMaterial.builder()
                .materialName(requestDTO.getMaterialName())
                .calorie(requestDTO.getCalorie())
                .price(requestDTO.getPrice())
                .img(requestDTO.getImg())
                .status("active")
                .build();

        return toResponseDTO(mainMaterialRepository.save(mainMaterial));
    }

    // 재료 삭제
    public void removeMainMaterial(String materialName) {
        if (!mainMaterialRepository.existsByMaterialName(materialName)) {
            throw new MainMaterialNotFoundException(materialName);
        }
        mainMaterialRepository.deleteByMaterialName(materialName);
    }

    // 재료 수정
    @Transactional
    public MainMaterialResponseDTO editMainMaterialDetails(String materialName, MainMaterialRequestDTO requestDTO) {
        MainMaterial existingMaterial = mainMaterialRepository.findByMaterialName(materialName)
                .orElseThrow(() -> new MainMaterialNotFoundException(materialName));

        MainMaterial updatedMaterial = MainMaterial.builder()
                .uid(existingMaterial.uid())
                .materialName(requestDTO.getMaterialName())
                .calorie(requestDTO.getCalorie())
                .price(requestDTO.getPrice())
                .img(requestDTO.getImg())
                .status(existingMaterial.status())
                .createdDate(existingMaterial.createdDate())
                .version(existingMaterial.version()) // 버전 증가
                .build();

        return toResponseDTO(mainMaterialRepository.save(updatedMaterial));
    }

    // 재료 상태 업데이트
    public void updateMainMaterialStatus(Long uid, String status) {
        if (!mainMaterialRepository.existsById(uid)) {
            throw new MainMaterialNotFoundException("ID: " + uid);
        }
        mainMaterialRepository.updateStatusByUid(uid, status);
    }

    // MainMaterial -> MainMaterialResponseDTO 변환 메서드
    private MainMaterialResponseDTO toResponseDTO(MainMaterial mainMaterial) {
        return new MainMaterialResponseDTO(
                mainMaterial.uid(),
                mainMaterial.materialName(),
                mainMaterial.calorie(),
                mainMaterial.price(),
                mainMaterial.img(),
                mainMaterial.status(),
                mainMaterial.createdDate(),
                mainMaterial.version()
        );
    }
}
