package com.bemtivi.bemtivi.application.business;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bemtivi.bemtivi.exceptions.InternalErrorException;
import com.bemtivi.bemtivi.exceptions.UnsupportedMediaTypeException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadBusiness {
    private final AmazonS3 amazonS3Client;

    public String uploadObject(MultipartFile objectFile) {
        String extension = getExtension(objectFile);

        if (!extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".webp")) {
            throw new UnsupportedMediaTypeException(RuntimeErrorEnum.ERR0018);
        }

        String fileName = getName(extension);
        String bucketName = "bemtivi-bucket";
        try {
            amazonS3Client.putObject(bucketName, fileName, objectFile.getInputStream(), getMetadata(objectFile));
        } catch (IOException e) {
            throw new InternalErrorException(RuntimeErrorEnum.ERR0012);
        }
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

    private ObjectMetadata getMetadata(MultipartFile objectFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(objectFile.getSize());
        metadata.setContentType(objectFile.getContentType());
        return  metadata;
    }

    private String getName(String extension) {
        return UUID.randomUUID() + extension;
    }

    private String getExtension(MultipartFile objectFile) {
        return Objects.requireNonNull(
                objectFile.getOriginalFilename()).substring(objectFile.getOriginalFilename().lastIndexOf(".")
        ).toLowerCase();
    }
}
