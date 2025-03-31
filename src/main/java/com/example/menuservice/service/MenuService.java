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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    // 메뉴 목록 조회
    public List<MenuResponseDTO> viewMenuList() {
        List<MenuResponseDTO> menuResponseDTOList = new ArrayList<>();
        for (Menu menu : menuRepository.findAll()) {
            menuResponseDTOList.add(toResponseDTO(menu));
        }
        return menuResponseDTOList;
    }

    // 메뉴 이름으로 메뉴 조회
    public MenuResponseDTO viewMenu(String menuName) {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));
        return toResponseDTO(menu);
    }

    // 메뉴 추가
    public MenuResponseDTO addMenu(MenuRequestDTO menuRequestDTO) {
        if (menuRepository.existsByMenuName(menuRequestDTO.getMenuName())) {
            throw new MenuAlreadyExistsException(menuRequestDTO.getMenuName());
        }

        Menu menu = Menu.builder()
                .menuName(menuRequestDTO.getMenuName())
                .price(menuRequestDTO.getPrice())
                .calorie(menuRequestDTO.getCalorie())
                .bread(menuRequestDTO.getBread())
                .mainMaterial1(menuRequestDTO.getMainMaterial1())
                .mainMaterial2(menuRequestDTO.getMainMaterial2())
                .mainMaterial3(menuRequestDTO.getMainMaterial3())
                .cheese(menuRequestDTO.getCheese())
                .vegetable1(menuRequestDTO.getVegetable1())
                .vegetable2(menuRequestDTO.getVegetable2())
                .vegetable3(menuRequestDTO.getVegetable3())
                .vegetable4(menuRequestDTO.getVegetable4())
                .vegetable5(menuRequestDTO.getVegetable5())
                .vegetable6(menuRequestDTO.getVegetable6())
                .vegetable7(menuRequestDTO.getVegetable7())
                .vegetable8(menuRequestDTO.getVegetable8())
                .sauce1(menuRequestDTO.getSauce1())
                .sauce2(menuRequestDTO.getSauce2())
                .sauce3(menuRequestDTO.getSauce3())
                .img(menuRequestDTO.getImg())
                .status("active")
                .build();

        Menu savedMenu = menuRepository.save(menu);
        return toResponseDTO(savedMenu);
    }

    // 메뉴 삭제
    public void removeMenu(String menuName) {
        if (!menuRepository.existsByMenuName(menuName)) {
            throw new MenuNotFoundException(menuName);
        }
        menuRepository.deleteByMenuName(menuName);
    }

    // 메뉴 수정
    @Transactional
    public MenuResponseDTO editMenuDetails(String menuName, MenuRequestDTO menuRequestDTO) {
        Menu existingMenu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));

        Menu updatedMenu = Menu.builder()
                .uid(existingMenu.uid())
                .menuName(menuRequestDTO.getMenuName())
                .price(menuRequestDTO.getPrice())
                .calorie(menuRequestDTO.getCalorie())
                .bread(menuRequestDTO.getBread())
                .mainMaterial1(menuRequestDTO.getMainMaterial1())
                .mainMaterial2(menuRequestDTO.getMainMaterial2())
                .mainMaterial3(menuRequestDTO.getMainMaterial3())
                .cheese(menuRequestDTO.getCheese())
                .vegetable1(menuRequestDTO.getVegetable1())
                .vegetable2(menuRequestDTO.getVegetable2())
                .vegetable3(menuRequestDTO.getVegetable3())
                .vegetable4(menuRequestDTO.getVegetable4())
                .vegetable5(menuRequestDTO.getVegetable5())
                .vegetable6(menuRequestDTO.getVegetable6())
                .vegetable7(menuRequestDTO.getVegetable7())
                .vegetable8(menuRequestDTO.getVegetable8())
                .sauce1(menuRequestDTO.getSauce1())
                .sauce2(menuRequestDTO.getSauce2())
                .sauce3(menuRequestDTO.getSauce3())
                .img(menuRequestDTO.getImg())
                .status(existingMenu.status())
                .createdDate(existingMenu.createdDate())
                .version(existingMenu.version()) // 버전 증가
                .build();

        Menu savedMenu = menuRepository.save(updatedMenu);
        return toResponseDTO(savedMenu);
    }

    // 메뉴 상태 업데이트
    public void updateMenuStatus(Long uid, String status) {
        if (!menuRepository.existsById(uid)) {
            throw new MenuNotFoundException("ID: " + uid);
        }
        menuRepository.updateStatusByUid(uid, status);
    }

    // Menu -> MenuResponseDTO 변환 메서드
    private MenuResponseDTO toResponseDTO(Menu menu) {
        return new MenuResponseDTO(
                menu.uid(),
                menu.menuName(),
                menu.price(),
                menu.calorie(),
                menu.bread(),
                menu.mainMaterial1(),
                menu.mainMaterial2(),
                menu.mainMaterial3(),
                menu.cheese(),
                menu.vegetable1(),
                menu.vegetable2(),
                menu.vegetable3(),
                menu.vegetable4(),
                menu.vegetable5(),
                menu.vegetable6(),
                menu.vegetable7(),
                menu.vegetable8(),
                menu.sauce1(),
                menu.sauce2(),
                menu.sauce3(),
                menu.img(),
                menu.status(),
                menu.createdDate(),
                menu.version()
        );
    }
}
