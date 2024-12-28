package com.example.panttegi.file.controller;

import com.example.panttegi.common.CommonListResDto;
import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.file.dto.FileResponseDto;
import com.example.panttegi.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<CommonResDto<FileResponseDto>> uploadFile(
            @RequestParam MultipartFile file,
            @RequestParam Long cardId,
            Authentication authentication
    ) {
        FileResponseDto response = fileService.uploadFile(file, cardId, authentication.getName());
        return new ResponseEntity<>(new CommonResDto<>("파일 업로드 완료", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonListResDto<FileResponseDto>> getFiles(@RequestParam Long cardId) {
        List<FileResponseDto> response = fileService.getFiles(cardId);
        return new ResponseEntity<>(new CommonListResDto<>("파일 조회 완료", response), HttpStatus.OK);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long fileId,
            Authentication authentication
    ) {
        fileService.deleteFile(fileId, authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}