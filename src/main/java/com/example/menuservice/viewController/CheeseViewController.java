package com.example.menuservice.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheeseViewController {

    // 치즈 관리 페이지로 이동
    @GetMapping("/cheeses/admin")
    public String index() {
        return "cheeseAdmin";  // cheeseAdmin.html 페이지로 이동
    }

    // 치즈 목록 조회 페이지로 이동
    @GetMapping("/cheeses/list")
    public String viewCheeseList() {
        return "cheeseList";  // cheeseList.html 페이지로 이동
    }

    // 특정 치즈 수정 페이지로 이동
    @GetMapping("/cheeses/edit/{cheeseName}")
    public String editCheese() {
        return "cheeseEdit";  // cheeseEdit.html 페이지로 이동
    }
}
