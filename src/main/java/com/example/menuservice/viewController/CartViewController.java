package com.example.menuservice.viewController;

import com.example.menuservice.domain.Cart;
import com.example.menuservice.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartViewController {

    private final CartService cartService;

    // 장바구니 페이지 렌더링
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        String sessionId = session.getId();
        List<Cart> cartItems = cartService.getCartItems(sessionId);

        int totalQuantity = cartItems.stream().mapToInt(Cart::getAmount).sum();
        long totalPrice = cartItems.stream().mapToLong(item -> item.getAmount() * item.getPrice()).sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalPrice", totalPrice);
        return "cartList";  // Thymeleaf 템플릿 이름
    }
}
