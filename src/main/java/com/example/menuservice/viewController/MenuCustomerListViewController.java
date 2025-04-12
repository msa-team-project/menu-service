package com.example.menuservice.viewController;

import com.example.menuservice.dto.MenuResponseDTO;
import com.example.menuservice.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MenuCustomerListViewController {

    private final MenuService menuService;

    // 고객용 메뉴 목록 페이지
    @GetMapping("/")
    public String showCustomerMenuList(Model model) {
        List<MenuResponseDTO> menus = menuService.viewMenuList();  // 활성 상태만 필터링도 가능
        model.addAttribute("menus", menus);
        return "menuCustomerList";
    }
}
