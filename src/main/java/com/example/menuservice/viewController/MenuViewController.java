package com.example.menuservice.viewController;

import com.example.menuservice.dto.MenuResponseDTO;
import com.example.menuservice.repository.*;
import com.example.menuservice.service.MenuService;
import com.example.menuservice.status.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuViewController {

    private final MenuService menuService;
    private final BreadRepository breadRepository;
    private final MaterialRepository materialRepository;
    private final CheeseRepository cheeseRepository;
    private final VegetableRepository vegetableRepository;
    private final SauceRepository sauceRepository;

    /**
     * 공통 선택 옵션 모델에 추가
     */
    private void addIngredientOptions(Model model) {
        model.addAttribute("breads", breadRepository.findByStatus(BreadStatus.ACTIVE.name()));
        model.addAttribute("materials", materialRepository.findByStatus(MaterialStatus.ACTIVE.name()));
        model.addAttribute("cheeses", cheeseRepository.findByStatus(CheeseStatus.ACTIVE.name()));
        model.addAttribute("vegetables", vegetableRepository.findByStatus(VegetableStatus.ACTIVE.name()));
        model.addAttribute("sauces", sauceRepository.findByStatus(SauceStatus.ACTIVE.name()));
    }


    /**
     * 메뉴 등록 페이지
     */
    @GetMapping("/menus/admin")
    public String showMenuForm(Model model) {
        addIngredientOptions(model);
        return "menuAdmin";
    }

    @GetMapping("/menus/list")
    public String showMenuList(Model model) {
        List<MenuResponseDTO> menus = menuService.viewMenuList();
        model.addAttribute("menus", menus);
        return "menuList"; // 확장자 없이 thymeleaf 템플릿 이름만
    }

    @GetMapping("/menus/edit/{menuName}")
    public String editMenu(@PathVariable String menuName, Model model) {
        MenuResponseDTO menu = menuService.viewMenu(menuName);
        model.addAttribute("menu", menu);
        model.addAttribute("imgUrl", menu.getImg());
        addIngredientOptions(model);
        return "menuEdit";
    }
}
