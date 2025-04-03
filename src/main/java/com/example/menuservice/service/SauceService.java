package com.example.menuservice.service;

import com.example.menuservice.domain.Sauce;
import com.example.menuservice.dto.SauceRequestDTO;
import com.example.menuservice.dto.SauceResponseDTO;
import com.example.menuservice.exception.SauceAlreadyExistsException;
import com.example.menuservice.exception.SauceNotFoundException;
import com.example.menuservice.repository.SauceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SauceService {
    private final SauceRepository sauceRepository;

    // 소스 목록 조회
    public List<SauceResponseDTO> viewSauceList() {
        return sauceRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 소스 이름으로 조회
    public SauceResponseDTO viewSauce(String sauceName) {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));
        return toResponseDTO(sauce);
    }

    // 소스 추가
    @Transactional
    public SauceResponseDTO addSauce(SauceRequestDTO requestDTO) {
        if (sauceRepository.existsBySauceName(requestDTO.getSauceName())) {
            throw new SauceAlreadyExistsException(requestDTO.getSauceName());
        }

        Sauce sauce = Sauce.builder()
                .sauceName(requestDTO.getSauceName())
                .calorie(requestDTO.getCalorie())
                .price(requestDTO.getPrice())
                .img(requestDTO.getImg())
                .status("active")
                .build();

        return toResponseDTO(sauceRepository.save(sauce));
    }

    // 소스 삭제
    @Transactional
    public void removeSauce(String sauceName) {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));
        sauceRepository.delete(sauce);
    }

    // 소스 수정 (변경 감지 활용)
    @Transactional
    public SauceResponseDTO editSauceDetails(String sauceName, SauceRequestDTO requestDTO) {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));

        // ✅ 변경 감지를 활용한 업데이트
        sauce.updateSauce(
                requestDTO.getSauceName(),
                requestDTO.getCalorie(),
                requestDTO.getPrice(),
                requestDTO.getImg()
        );

        return toResponseDTO(sauce);
    }

    // 소스 상태 업데이트
    @Transactional
    public void updateSauceStatus(Long uid, String status) {
        Sauce sauce = sauceRepository.findById(uid)
                .orElseThrow(() -> new SauceNotFoundException("ID: " + uid));
        sauce.setStatus(status);
    }

    // Sauce -> SauceResponseDTO 변환 메서드
    private SauceResponseDTO toResponseDTO(Sauce sauce) {
        return new SauceResponseDTO(
                sauce.getUid(),
                sauce.getSauceName(),
                sauce.getCalorie(),
                sauce.getPrice(),
                sauce.getStatus(),
                sauce.getImg(),
                sauce.getCreatedDate(),
                sauce.getVersion()
        );
    }
}
