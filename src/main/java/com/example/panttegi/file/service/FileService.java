package com.example.panttegi.file.service;

import com.example.panttegi.card.entity.Card;
import com.example.panttegi.card.repository.CardRepository;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.file.dto.FileResponseDto;
import com.example.panttegi.file.entity.File;
import com.example.panttegi.file.repository.FileRepository;
import com.example.panttegi.s3.S3Uploader;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public List<FileResponseDto> uploadFiles(List<MultipartFile> files, Long cardId, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);
        Card card = cardRepository.findByIdOrElseThrow(cardId);

        List<File> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileUrl = s3Uploader.uploadFile(file);
            File uploadedFile = fileRepository.save(new File(fileUrl, user, card));
            uploadedFiles.add(uploadedFile);
        }

        return uploadedFiles.stream()
                .map(FileResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<FileResponseDto> getFiles(Long cardId) {
        List<File> files = fileRepository.findAllByCardId(cardId);
        return files.stream()
                .map(FileResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFile(Long fileId, String email) {
        File file = fileRepository.findByIdOrElseThrow(fileId);
        User user = userRepository.findByEmailOrElseThrow(email);

        if (!file.getUser().equals(user)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        s3Uploader.deleteFile(file.getUrl());
        fileRepository.delete(file);
    }
}
