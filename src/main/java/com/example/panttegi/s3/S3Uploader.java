package com.example.panttegi.s3;

import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;
    private final String bucketName = "pan-ttegi-bucket";

    public String uploadFile(MultipartFile file) {
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;

        System.out.println("Original filename: " + originalFilename);
        System.out.println("Generated unique filename: " + uniqueFilename);

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(uniqueFilename)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, uniqueFilename);
    }

    public void deleteFile(String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl);
        try {
            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(fileName).build());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

    private void validateFile(MultipartFile file) {
        long maxFileSize = 5 * 1024 * 1024;
        String[] allowedExtensions = {"jpg", "png", "pdf", "csv"};

        if (file.getSize() > maxFileSize) {
            throw new CustomException(ErrorCode.LARGE_FILE);
        }

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
        if (!List.of(allowedExtensions).contains(extension)) {
            throw new CustomException(ErrorCode.INVALID_FILE);
        }
    }
}
