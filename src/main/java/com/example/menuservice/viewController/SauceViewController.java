package com.example.menuservice.viewController;

import com.example.menuservice.dto.SauceResponseDTO;
import com.example.menuservice.service.SauceService;
import com.example.menuservice.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SauceViewController {

    private final SauceService sauceService;
    private final FileUploadService fileUploadService;

    // ✅ 소스 업로드 페이지로 이동
    @GetMapping("/sauces/admin")
    public String index(Model model) {
        List<String> imageUrls = fileUploadService.getUploadedImages();
        model.addAttribute("imageUrls", imageUrls);
        return "sauceAdmin";  // Thymeleaf 템플릿 (sauceAdmin.html)
    }

    // ✅ 소스 목록 조회 페이지
    @GetMapping("/sauces/list")
    public String viewSauceList(Model model) {
        model.addAttribute("sauces", sauceService.viewSauceList());
        return "sauceList"; // ✅ sauceList.html 파일로 이동
    }

    // ✅ 특정 소스 수정 페이지
    @GetMapping("/sauces/edit/{sauceName}")
    public String editSauce(@PathVariable String sauceName, Model model) {
        SauceResponseDTO sauce = sauceService.viewSauce(sauceName);
        model.addAttribute("sauce", sauce);
        return "sauceEdit";  // ✅ sauceEdit.html 파일로 이동
    }
}
