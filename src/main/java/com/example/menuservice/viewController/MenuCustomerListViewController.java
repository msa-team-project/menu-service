package com.example.menuservice.viewController;

import com.example.menuservice.dto.MenuResponseDTO;
import com.example.menuservice.service.CartService;
import com.example.menuservice.service.MenuService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MenuCustomerListViewController {

    private final MenuService menuService;
    private final CartService cartService;

    // 고객용 메뉴 목록 페이지
    @GetMapping("/")
    public String showCustomerMenuList(Model model) {
        List<MenuResponseDTO> menus = menuService.viewMenuList();  // 활성 상태만 필터링도 가능
        model.addAttribute("menus", menus);
        return "menuCustomerList";
    }
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("menuId") Long menuId,
                            @RequestParam("amount") int amount,
                            HttpSession session) {
        cartService.addToCart(menuId, amount, session);
        return "redirect:/cart";  // ✅ 장바구니 목록 불러오는 GET 요청으로 리디렉션
    }


}
