package com.example.menuservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
public class StoreController {

    @GetMapping("/register")
    public String register() {
        return "store-register";
    }

    @GetMapping("/storelist")
    public String storelist() {
        return "store-list";
    }
}
