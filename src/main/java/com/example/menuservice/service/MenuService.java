package com.example.menuservice.service;

import com.example.menuservice.domain.*;
import com.example.menuservice.dto.MenuRequestDTO;
import com.example.menuservice.dto.MenuResponseDTO;
import com.example.menuservice.exception.MenuAlreadyExistsException;
import com.example.menuservice.exception.MenuNotFoundException;
import com.example.menuservice.repository.*;
import com.example.menuservice.status.MenuStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final FileUploadService fileUploadService;

    private final BreadRepository breadRepository;
    private final MaterialRepository materialRepository;
    private final CheeseRepository cheeseRepository;
    private final VegetableRepository vegetableRepository;
    private final SauceRepository sauceRepository;

    public List<MenuResponseDTO> viewMenuList() {


        return menuRepository.findAll().stream()
                .map(MenuResponseDTO::fromEntity)
                .collect(Collectors.toList());

    }

    public MenuResponseDTO viewMenu(String menuName) {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));
        return MenuResponseDTO.fromEntity(menu);
    }

    @Transactional
    public MenuResponseDTO addMenu(MenuRequestDTO dto, MultipartFile file) throws IOException {
        String fileUrl = null;



        if (menuRepository.existsByMenuName(dto.getMenuName())) {
            throw new MenuAlreadyExistsException(dto.getMenuName());
        }

        try {
            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            MenuStatus status = MenuStatus.valueOf(dto.getStatus().toUpperCase());

            Menu menu = Menu.builder()
                    .menuName(dto.getMenuName())
                    .price(dto.getPrice())
                    .calorie(dto.getCalorie())
                    .bread(getBread(dto.getBread()))
                    .material1(getMaterial(dto.getMaterial1()))
                    .material2(getOptionalMaterial(dto.getMaterial2()))
                    .material3(getOptionalMaterial(dto.getMaterial3()))
                    .cheese(getOptionalCheese(dto.getCheese()))
                    .vegetable1(getVegetable(dto.getVegetable1()))
                    .vegetable2(getOptionalVegetable(dto.getVegetable2()))
                    .vegetable3(getOptionalVegetable(dto.getVegetable3()))
                    .vegetable4(getOptionalVegetable(dto.getVegetable4()))
                    .vegetable5(getOptionalVegetable(dto.getVegetable5()))
                    .vegetable6(getOptionalVegetable(dto.getVegetable6()))
                    .vegetable7(getOptionalVegetable(dto.getVegetable7()))
                    .vegetable8(getOptionalVegetable(dto.getVegetable8()))
                    .sauce1(getSauce(dto.getSauce1()))
                    .sauce2(getOptionalSauce(dto.getSauce2()))
                    .sauce3(getOptionalSauce(dto.getSauce3()))
                    .img(fileUrl)
                    .status(status.name())
                    .build();
            return MenuResponseDTO.fromEntity(menuRepository.save(menu));
        } catch (Exception e) {
            if (fileUrl != null) fileUploadService.deleteFile(fileUrl);
            throw e;
        }
    }

    @Transactional
    public MenuResponseDTO editMenuDetails(String menuName, MenuRequestDTO dto, MultipartFile file) throws IOException {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));

        String fileUrl = menu.getImg();

        try {
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) fileUploadService.deleteFile(fileUrl);
                fileUrl = fileUploadService.uploadFile(file);
            }

            menu.updateMenu(
                    dto.getMenuName(),
                    dto.getPrice(),
                    dto.getCalorie(),
                    getBread(dto.getBread()),
                    getMaterial(dto.getMaterial1()),
                    getOptionalMaterial(dto.getMaterial2()),
                    getOptionalMaterial(dto.getMaterial3()),
                    getOptionalCheese(dto.getCheese()),
                    getVegetable(dto.getVegetable1()),
                    getOptionalVegetable(dto.getVegetable2()),
                    getOptionalVegetable(dto.getVegetable3()),
                    getOptionalVegetable(dto.getVegetable4()),
                    getOptionalVegetable(dto.getVegetable5()),
                    getOptionalVegetable(dto.getVegetable6()),
                    getOptionalVegetable(dto.getVegetable7()),
                    getOptionalVegetable(dto.getVegetable8()),
                    getSauce(dto.getSauce1()),
                    getOptionalSauce(dto.getSauce2()),
                    getOptionalSauce(dto.getSauce3()),
                    fileUrl,
                    dto.getStatus()
            );

            return MenuResponseDTO.fromEntity(menu);
        } catch (Exception e) {
            if (fileUrl != null) fileUploadService.deleteFile(fileUrl);
            throw e;
        }
    }

    @Transactional
    public void removeMenu(String menuName) {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));
        menu.setStatus(MenuStatus.DELETED.name());
    }

    @Transactional
    public void updateMenuStatus(Long uid, String status) {
        Menu menu = menuRepository.findById(uid)
                .orElseThrow(() -> new MenuNotFoundException("ID: " + uid));
        menu.setStatus(status);
    }

    // === Entity fetch helpers ===
    private Bread getBread(Long id) {
        return breadRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Bread not found: " + id));
    }

    private Material getMaterial(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Material not found: " + id));
    }

    private Material getOptionalMaterial(Long id) {
        return id != null ? getMaterial(id) : null;
    }

    private Cheese getOptionalCheese(Long id) {
        return id != null ? cheeseRepository.findById(id).orElse(null) : null;
    }

    private Vegetable getVegetable(Long id) {
        return vegetableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Vegetable not found: " + id));
    }

    private Vegetable getOptionalVegetable(Long id) {
        return id != null ? getVegetable(id) : null;
    }

    private Sauce getSauce(Long id) {
        return sauceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sauce not found: " + id));
    }

    private Sauce getOptionalSauce(Long id) {
        return id != null ? getSauce(id) : null;
    }
}
