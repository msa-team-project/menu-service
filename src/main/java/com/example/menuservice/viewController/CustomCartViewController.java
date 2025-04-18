package com.example.menuservice.viewController;

import com.example.menuservice.dto.CustomCartResponseDTO;
import com.example.menuservice.service.CartService;
import com.example.menuservice.service.CustomCartService;
import com.example.menuservice.repository.*;
import com.example.menuservice.service.SauceService;
import com.example.menuservice.status.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CustomCartViewController {

    private final CustomCartService customCartService;
    private final BreadRepository breadRepository;
    private final MaterialRepository materialRepository;
    private final CheeseRepository cheeseRepository;
    private final VegetableRepository vegetableRepository;
    private final SauceRepository sauceRepository;
    private final CartService cartService;


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
     * 사용자 맞춤형 샌드위치 페이지
     */
//    @GetMapping("/customCart/{uid}")
//    public String viewCustomCart(@PathVariable Long uid, Model model) {
//        CustomCartResponseDTO customCart = customCartService.viewCustomCart(uid);
//        model.addAttribute("customCart", customCart);
//        addIngredientOptions(model);
//        return "customCart";  // 페이지 이름 변경
//    }

    /**
     * 사용자 맞춤형 샌드위치 추가 페이지
     */
    @GetMapping("/customCart")  // 경로 변경
    public String showCustomCartForm(Model model) {
        addIngredientOptions(model);
        return "customCart";  // 페이지 이름 변경
    }

}
