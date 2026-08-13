package com.healthcare.clinic.document.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface DocumentStorageService {
    
    /**
     * Uploads a file to the storage backend.
     * @param file The multipart file to upload
     * @return The storage key (e.g. S3 object key or relative local path)
     */
    String uploadFile(MultipartFile file);

    /**
     * Retrieves the file as an input stream.
     * @param storageKey The storage key of the file
     * @return InputStream of the file content
     */
    InputStream downloadFile(String storageKey);

    /**
     * Generates a pre-signed URL for direct download if supported by the backend.
     * @param storageKey The storage key of the file
     * @return The pre-signed URL or a direct link
     */
    String generateDownloadUrl(String storageKey);

    /**
     * Deletes the file from the storage backend.
     * @param storageKey The storage key of the file
     */
    void deleteFile(String storageKey);
}
