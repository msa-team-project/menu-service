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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SauceService {
    private final SauceRepository sauceRepository;

    // 소스 목록 조회
    public List<SauceResponseDTO> viewSauceList() {
        List<SauceResponseDTO> sauceResponseDTOList = new ArrayList<>();
        for (Sauce sauce : sauceRepository.findAll()) {
            sauceResponseDTOList.add(toResponseDTO(sauce));
        }
        return sauceResponseDTOList;
    }

    // 소스 이름으로 소스 조회
    public SauceResponseDTO viewSauce(String sauceName) {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));
        return toResponseDTO(sauce);
    }

    // 소스 추가
    public SauceResponseDTO addSauce(SauceRequestDTO sauceRequestDTO) {
        if (sauceRepository.existsBySauceName(sauceRequestDTO.getSauceName())) {
            throw new SauceAlreadyExistsException(sauceRequestDTO.getSauceName());
        }

        Sauce sauce = Sauce.builder()
                .sauceName(sauceRequestDTO.getSauceName())
                .calorie(sauceRequestDTO.getCalorie())
                .price(sauceRequestDTO.getPrice())
                .img(sauceRequestDTO.getImg())
                .status("active")
                .build();

        Sauce savedSauce = sauceRepository.save(sauce);
        return toResponseDTO(savedSauce);
    }

    // 소스 삭제
    public void removeSauce(String sauceName) {
        if (!sauceRepository.existsBySauceName(sauceName)) {
            throw new SauceNotFoundException(sauceName);
        }
        sauceRepository.deleteBySauceName(sauceName);
    }

    // 소스 수정
    @Transactional
    public SauceResponseDTO editSauceDetails(String sauceName, SauceRequestDTO sauceRequestDTO) {
        Sauce existingSauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));

        Sauce updatedSauce = Sauce.builder()
                .uid(existingSauce.uid())
                .sauceName(sauceRequestDTO.getSauceName())
                .calorie(sauceRequestDTO.getCalorie())
                .price(sauceRequestDTO.getPrice())
                .img(sauceRequestDTO.getImg())
                .status(existingSauce.status())
                .createdDate(existingSauce.createdDate())
                .version(existingSauce.version()) // 버전 증가
                .build();

        Sauce savedSauce = sauceRepository.save(updatedSauce);
        return toResponseDTO(savedSauce);
    }

    // 소스 상태 업데이트
    public void updateSauceStatus(Long uid, String status) {
        if (!sauceRepository.existsById(uid)) {
            throw new SauceNotFoundException("ID: " + uid);
        }
        sauceRepository.updateStatusByUid(uid, status);
    }

    // Sauce -> SauceResponseDTO 변환 메서드
    private SauceResponseDTO toResponseDTO(Sauce sauce) {
        return new SauceResponseDTO(
                sauce.uid(),
                sauce.sauceName(),
                sauce.calorie(),
                sauce.price(),
                sauce.status(),
                sauce.img(),
                sauce.createdDate(),
                sauce.version()
        );
    }
}
