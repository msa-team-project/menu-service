package com.example.menuservice.service;

import com.example.menuservice.domain.Menu;
import com.example.menuservice.dto.MenuRequestDTO;
import com.example.menuservice.dto.MenuResponseDTO;
import com.example.menuservice.exception.MenuAlreadyExistsException;
import com.example.menuservice.exception.MenuNotFoundException;
import com.example.menuservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    // 메뉴 목록 조회
    public List<MenuResponseDTO> viewMenuList() {
        return menuRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 메뉴 이름으로 메뉴 조회
    public MenuResponseDTO viewMenu(String menuName) {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));
        return toResponseDTO(menu);
    }

    // 메뉴 추가
    @Transactional
    public MenuResponseDTO addMenu(MenuRequestDTO requestDTO) {
        if (menuRepository.existsByMenuName(requestDTO.getMenuName())) {
            throw new MenuAlreadyExistsException(requestDTO.getMenuName());
        }

        Menu menu = Menu.builder()
                .menuName(requestDTO.getMenuName())
                .price(requestDTO.getPrice())
                .calorie(requestDTO.getCalorie())
                .bread(requestDTO.getBread())
                .material1(requestDTO.getMaterial1())
                .material2(requestDTO.getMaterial2())
                .material3(requestDTO.getMaterial3())
                .cheese(requestDTO.getCheese())
                .vegetable1(requestDTO.getVegetable1())
                .vegetable2(requestDTO.getVegetable2())
                .vegetable3(requestDTO.getVegetable3())
                .vegetable4(requestDTO.getVegetable4())
                .vegetable5(requestDTO.getVegetable5())
                .vegetable6(requestDTO.getVegetable6())
                .vegetable7(requestDTO.getVegetable7())
                .vegetable8(requestDTO.getVegetable8())
                .sauce1(requestDTO.getSauce1())
                .sauce2(requestDTO.getSauce2())
                .sauce3(requestDTO.getSauce3())
                .img(requestDTO.getImg())
                .status("active")
                .build();

        return toResponseDTO(menuRepository.save(menu));
    }

    // 메뉴 삭제
    @Transactional
    public void removeMenu(String menuName) {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));
        menuRepository.delete(menu);
    }

    // 메뉴 수정 (변경 감지 활용)
    @Transactional
    public MenuResponseDTO editMenuDetails(String menuName, MenuRequestDTO requestDTO) {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));

        // ✅ 변경 감지를 활용해 자동 업데이트
        menu.updateMenu(
                requestDTO.getMenuName(),
                requestDTO.getPrice(),
                requestDTO.getCalorie(),
                requestDTO.getBread(),
                requestDTO.getMaterial1(),
                requestDTO.getMaterial2(),
                requestDTO.getMaterial3(),
                requestDTO.getCheese(),
                requestDTO.getVegetable1(),
                requestDTO.getVegetable2(),
                requestDTO.getVegetable3(),
                requestDTO.getVegetable4(),
                requestDTO.getVegetable5(),
                requestDTO.getVegetable6(),
                requestDTO.getVegetable7(),
                requestDTO.getVegetable8(),
                requestDTO.getSauce1(),
                requestDTO.getSauce2(),
                requestDTO.getSauce3(),
                requestDTO.getImg()
        );

        return toResponseDTO(menu);
    }

    // 메뉴 상태 업데이트
    @Transactional
    public void updateMenuStatus(Long uid, String status) {
        Menu menu = menuRepository.findById(uid)
                .orElseThrow(() -> new MenuNotFoundException("ID: " + uid));
        menu.setStatus(status);
    }

    // Menu -> MenuResponseDTO 변환 메서드
    private MenuResponseDTO toResponseDTO(Menu menu) {
        return new MenuResponseDTO(
                menu.getUid(),
                menu.getMenuName(),
                menu.getPrice(),
                menu.getCalorie(),
                menu.getBread(),
                menu.getMaterial1(),
                menu.getMaterial2(),
                menu.getMaterial3(),
                menu.getCheese(),
                menu.getVegetable1(),
                menu.getVegetable2(),
                menu.getVegetable3(),
                menu.getVegetable4(),
                menu.getVegetable5(),
                menu.getVegetable6(),
                menu.getVegetable7(),
                menu.getVegetable8(),
                menu.getSauce1(),
                menu.getSauce2(),
                menu.getSauce3(),
                menu.getImg(),
                menu.getStatus(),
                menu.getCreatedDate(),
                menu.getVersion()
        );
    }
}
