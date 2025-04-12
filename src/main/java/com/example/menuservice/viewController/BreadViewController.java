package com.example.menuservice.viewController;

import com.example.menuservice.dto.BreadResponseDTO;
import com.example.menuservice.service.BreadService;
import com.example.menuservice.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BreadViewController {

    private final BreadService breadService;
    private final FileUploadService fileUploadService;


    // 업로드 페이지로 이동
    @GetMapping("/breads/admin")
    public String index(Model model) {
        List<String> imageUrls = fileUploadService.getUploadedImages();
        model.addAttribute("imageUrls", imageUrls);
        return "breadAdmin";  // Thymeleaf 템플릿
    }

    // 빵 목록 조회 페이지
    @GetMapping("/breads/list")
    public String viewBreadList(Model model) {
        model.addAttribute("breads", breadService.viewBreadList());
        return "breadList"; // ✅ breadList.html 파일로 이동
    }



    @GetMapping("/breads/edit/{breadName}")
    public String editBread(@PathVariable String breadName, Model model) {
        BreadResponseDTO bread = breadService.viewBread(breadName);
        model.addAttribute("bread", bread);
        return "breadEdit";
    }
}

