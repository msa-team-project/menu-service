package com.example.menuservice.viewController;

import com.example.menuservice.dto.CheeseResponseDTO;
import com.example.menuservice.service.CheeseService;
import com.example.menuservice.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class cheeseViewController {

    private final CheeseService cheeseService;
    private final FileUploadService fileUploadService;

    // ✅ 치즈 업로드 페이지로 이동
    @GetMapping("/cheeses/admin")
    public String index(Model model) {
        List<String> imageUrls = fileUploadService.getUploadedImages();
        model.addAttribute("imageUrls", imageUrls);
        return "cheeseAdmin";  // Thymeleaf 템플릿 (cheeseAdmin.html)
    }

    // ✅ 치즈 목록 조회 페이지
    @GetMapping("/cheeses/list")
    public String viewCheeseList(Model model) {
        model.addAttribute("cheeses", cheeseService.viewCheeseList());
        return "cheeseList"; // ✅ cheeseList.html 파일로 이동
    }

    // ✅ 특정 치즈 수정 페이지
    @GetMapping("/cheeses/edit/{cheeseName}")
    public String editCheese(@PathVariable String cheeseName, Model model) {
        CheeseResponseDTO cheese = cheeseService.viewCheese(cheeseName);
        model.addAttribute("cheese", cheese);
        return "cheeseEdit";  // ✅ cheeseEdit.html 파일로 이동
    }
}
