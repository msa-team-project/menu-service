package com.example.menuservice.controller;

import com.example.menuservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    @Value("${kakao.rest.api-key}")
    private String kakaoApiKey;

    private final StoreService storeService;

    @GetMapping("/register")
    public String register(Model model)
    {
        model.addAttribute("kakaoApiKey",kakaoApiKey);
        return "store-register";
    }

    @GetMapping("/storelist")
    public String storelist() {
        return "store-list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam(name ="uid") Long uid, Model model) {
        model.addAttribute("uid", uid); // ✅ 꼭 필요함
        return "store-detail";
    }
}
