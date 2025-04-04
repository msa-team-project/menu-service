package com.example.menuservice.viewController;

import com.example.menuservice.repository.*;
import com.example.menuservice.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class menuViewController {

    private final MenuService menuService;
    private final BreadRepository breadRepository;
    private final MaterialRepository materialRepository;
    private final CheeseRepository cheeseRepository;
    private final VegetableRepository vegetableRepository;
    private final SauceRepository sauceRepository;

    // 메뉴 등록 폼 페이지
    @GetMapping("/menus/admin")
    public String showMenuForm(Model model) {
        model.addAttribute("breads", breadRepository.findAll());
        model.addAttribute("materials", materialRepository.findAll());
        model.addAttribute("cheeses", cheeseRepository.findAll());
        model.addAttribute("vegetables", vegetableRepository.findAll());
        model.addAttribute("sauces", sauceRepository.findAll());
        return "menuAdmin"; // ✅ menuForm.html 템플릿으로 이동
    }

    // 메뉴 목록 페이지
    @GetMapping("/menus/list")
    public String viewMenuList(Model model) {
        model.addAttribute("menus", menuService.viewMenuList());
        return "menuList"; // ✅ menuList.html 템플릿으로 이동
    }

    // 메뉴 수정 페이지
    @GetMapping("/menus/edit/{menuName}")
    public String editMenu(@PathVariable String menuName, Model model) {
        model.addAttribute("menu", menuService.viewMenu(menuName));
        return "menuEdit"; // ✅ menuEdit.html 템플릿으로 이동
    }
}
