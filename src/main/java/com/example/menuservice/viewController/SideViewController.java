package com.example.menuservice.viewController;

import com.example.menuservice.dto.SauceResponseDTO;
import com.example.menuservice.dto.SideResponseDTO;
import com.example.menuservice.service.FileUploadService;
import com.example.menuservice.service.SideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SideViewController {

    private final SideService sideService;
    private final FileUploadService fileUploadService;

    // ✅ 소스 업로드 페이지로 이동
    @GetMapping("/sides/admin")
    public String index(Model model) {
        List<String> imageUrls = fileUploadService.getUploadedImages();
        model.addAttribute("imageUrls", imageUrls);
        return "sideAdmin";  // Thymeleaf 템플릿 (sideAdmin.html)
    }

    // ✅ 소스 목록 조회 페이지
    @GetMapping("/sides/list")
    public String viewSideList(Model model) {
        model.addAttribute("sides", sideService.viewSideList());
        return "sideList"; // ✅ sideList.html 파일로 이동
    }

    // ✅ 특정 소스 수정 페이지
    @GetMapping("/sides/edit/{sideName}")
    public String editSide(@PathVariable String sideName, Model model) {
        SideResponseDTO side = sideService.viewSide(sideName);
        model.addAttribute("side", side);
        return "sideEdit";  // ✅ sauceEdit.html 파일로 이동
    }
}
