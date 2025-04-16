package com.example.menuservice.controller;

import com.example.menuservice.dto.store.StoreResponseDTO;
import com.example.menuservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/register")
    public String register() {
        return "store-register";
    }

    @GetMapping("/storelist")
    public String storelist() {
        return "store-list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam(name ="uid") Long uid, Model model) {
        StoreResponseDTO store = storeService.viewStore(uid);
        model.addAttribute("store", store); // ✅ 꼭 필요함
        return "store-detail";
    }
}
