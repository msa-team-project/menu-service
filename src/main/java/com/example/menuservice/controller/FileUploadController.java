package com.example.menuservice.controller;

import com.example.menuservice.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;


    // 이미지 업로드 처리
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            String fileUrl = fileUploadService.uploadFile(file);
            response.put("success", true);
            response.put("url", fileUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }




    // S3에서 이미지 목록을 가져오는 메서드
    @GetMapping("/images")
    @ResponseBody
    public List<String> getUploadedImages() {
        return fileUploadService.getUploadedImages();
    }

    // 이미지 삭제 처리
    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteFile(@RequestParam("fileName") String fileName) {
        Map<String, Object> response = new HashMap<>();

        try {
            fileUploadService.deleteFile(fileName);
            response.put("success", true);
            response.put("message", "파일이 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
