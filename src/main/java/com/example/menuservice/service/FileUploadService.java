package com.example.menuservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    // 파일 업로드 메서드
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        // UUID를 사용하여 고유한 파일명 생성
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

// 한글을 제거하거나 안전한 ASCII 문자로 대체
        String safeName = UUID.randomUUID() + extension;

        Path tempFile = Files.createTempFile("upload-", safeName);
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        String filePath = "uploads/" + safeName;
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(filePath)
                        .build(),
                tempFile
        );


        return "https://" + bucketName + ".s3.amazonaws.com/" + filePath;
    }

    // 업로드된 이미지 목록 가져오기
    public List<String> getUploadedImages() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix("uploads/")
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        return response.contents().stream()
                .map(S3Object::key)
                .map(key -> "https://" + bucketName + ".s3.amazonaws.com/" + key)
                .collect(Collectors.toList());
    }

    // 파일 삭제 메서드
    public void deleteFile(String fileUrl) {
        // ✅ URL에서 파일 이름만 추출 (Key 값 변환)
        String fileKey = fileUrl.replace("https://" + bucketName + ".s3.amazonaws.com/", "");

        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileKey) // ✅ S3 내부의 Key 값만 전달
                        .build()
        );
    }
}