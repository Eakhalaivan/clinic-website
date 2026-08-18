package com.healthcare.clinic.document.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.provider", havingValue = "s3")
public class S3StorageServiceImpl implements DocumentStorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket:clinic-documents}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String storageKey = UUID.randomUUID().toString() + "_" + originalFilename;

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("File uploaded successfully to S3 with key: {}", storageKey);
            return storageKey;
        } catch (IOException e) {
            log.error("Failed to upload file to S3", e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public InputStream downloadFile(String storageKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build();

            return s3Client.getObject(getObjectRequest);
        } catch (Exception e) {
            log.error("Failed to download file from S3: {}", storageKey, e);
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }

    @Override
    public String generateDownloadUrl(String storageKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .getObjectRequest(getObjectRequest)
                    .build();

            return s3Presigner.presignGetObject(presignRequest).url().toString();
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for S3 key: {}", storageKey, e);
            throw new RuntimeException("Failed to generate presigned URL for S3", e);
        }
    }

    @Override
    public void deleteFile(String storageKey) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted successfully from S3: {}", storageKey);
        } catch (Exception e) {
            log.error("Failed to delete file from S3: {}", storageKey, e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }
}
