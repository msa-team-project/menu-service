package com.example.menuservice.viewController;

import com.example.menuservice.dto.VegetableResponseDTO;
import com.example.menuservice.service.VegetableService;
import com.example.menuservice.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class vegetableViewController {

    private final VegetableService vegetableService;
    private final FileUploadService fileUploadService;

    // ✅ 채소 업로드 페이지로 이동
    @GetMapping("/vegetables/admin")
    public String index(Model model) {
        List<String> imageUrls = fileUploadService.getUploadedImages();
        model.addAttribute("imageUrls", imageUrls);
        return "vegetableAdmin";  // Thymeleaf 템플릿 (vegetableAdmin.html)
    }

    // ✅ 채소 목록 조회 페이지
    @GetMapping("/vegetables/list")
    public String viewVegetableList(Model model) {
        model.addAttribute("vegetables", vegetableService.viewVegetableList());
        return "vegetableList"; // ✅ vegetableList.html 파일로 이동
    }

    // ✅ 특정 채소 수정 페이지
    @GetMapping("/vegetables/edit/{vegetableName}")
    public String editVegetable(@PathVariable String vegetableName, Model model) {
        VegetableResponseDTO vegetable = vegetableService.viewVegetable(vegetableName);
        model.addAttribute("vegetable", vegetable);
        return "vegetableEdit";  // ✅ vegetableEdit.html 파일로 이동
    }
}
