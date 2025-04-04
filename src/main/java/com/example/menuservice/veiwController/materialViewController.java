package com.example.menuservice.viewController;

import com.example.menuservice.dto.MaterialResponseDTO;
import com.example.menuservice.service.MaterialService;
import com.example.menuservice.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class materialViewController {

    private final MaterialService materialService;
    private final FileUploadService fileUploadService;

    // ✅ 재료 업로드 페이지로 이동
    @GetMapping("/materials/admin")
    public String index(Model model) {
        List<String> imageUrls = fileUploadService.getUploadedImages();
        model.addAttribute("imageUrls", imageUrls);
        return "materialAdmin";  // Thymeleaf 템플릿 (materialAdmin.html)
    }

    // ✅ 재료 목록 조회 페이지
    @GetMapping("/materials/list")
    public String viewMaterialList(Model model) {
        model.addAttribute("materials", materialService.viewMaterialList());
        return "materialList"; // ✅ materialList.html 파일로 이동
    }

    // ✅ 특정 재료 수정 페이지
    @GetMapping("/materials/edit/{materialName}")
    public String editMaterial(@PathVariable String materialName, Model model) {
        MaterialResponseDTO material = materialService.viewMaterial(materialName);
        model.addAttribute("material", material);
        return "materialEdit";  // ✅ materialEdit.html 파일로 이동
    }
}
