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
public class MenuForCustomerController {

    private final MenuService menuService;


    @GetMapping("/menus/name/{menuName}")
    public String showMenuDetail(@PathVariable String menuName, Model model) {
        MenuResponseDTO menu = menuService.viewMenu(menuName);
        model.addAttribute("menu", menu);
        return "menuForCustomer"; // templates/menu/detail.html
    }


}



