package com.example.menuservice.viewController;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class MenuCustomerListViewController {



    // 고객용 메뉴 목록 페이지
    @GetMapping("/")
    public String showCustomerMenuList() {
        return "menuCustomerList"; // Model 제거
    }
}


