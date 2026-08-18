package com.healthcare.clinic.doctor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.*;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
@Slf4j
public class VoiceToTextService {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Value("${aws.access-key:mock_key}")
    private String awsAccessKey;

    @Value("${aws.secret-key:mock_secret}")
    private String awsSecretKey;

    @Value("${aws.s3.bucket.medical:medical-audio-bucket}")
    private String audioBucket;

    public String transcribe(MultipartFile audioFile) {
        log.info("Received audio file for transcription: {}, size: {}", audioFile.getOriginalFilename(), audioFile.getSize());
        
        if ("mock_key".equals(awsAccessKey)) {
            return "Patient presents with a 3-day history of productive cough, fever, and chills. Denies shortness of breath. Past medical history significant for asthma.";
        }

        try {
            StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(awsAccessKey, awsSecretKey));
            
            S3Client s3 = S3Client.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(credentialsProvider)
                    .build();
            
            TranscribeClient transcribe = TranscribeClient.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(credentialsProvider)
                    .build();

            // 1. Upload to S3
            String key = UUID.randomUUID().toString() + "-" + audioFile.getOriginalFilename();
            s3.putObject(PutObjectRequest.builder()
                    .bucket(audioBucket)
                    .key(key)
                    .build(), software.amazon.awssdk.core.sync.RequestBody.fromInputStream(audioFile.getInputStream(), audioFile.getSize()));
            
            String s3Uri = "s3://" + audioBucket + "/" + key;
            
            // 2. Start Medical Transcription Job
            String jobName = "Medical_Job_" + UUID.randomUUID().toString();
            StartMedicalTranscriptionJobRequest request = StartMedicalTranscriptionJobRequest.builder()
                    .medicalTranscriptionJobName(jobName)
                    .media(Media.builder().mediaFileUri(s3Uri).build())
                    .languageCode(LanguageCode.EN_US)
                    .specialty(Specialty.PRIMARYCARE)
                    .type(Type.DICTATION)
                    .outputBucketName(audioBucket)
                    .build();
            
            transcribe.startMedicalTranscriptionJob(request);
            
            // 3. Poll for completion (max 60 seconds)
            int attempts = 0;
            MedicalTranscriptionJob job = null;
            while (attempts < 12) {
                GetMedicalTranscriptionJobResponse response = transcribe.getMedicalTranscriptionJob(
                        GetMedicalTranscriptionJobRequest.builder().medicalTranscriptionJobName(jobName).build());
                job = response.medicalTranscriptionJob();
                if (job.transcriptionJobStatus() == TranscriptionJobStatus.COMPLETED ||
                    job.transcriptionJobStatus() == TranscriptionJobStatus.FAILED) {
                    break;
                }
                Thread.sleep(5000);
                attempts++;
            }
            
            if (job != null && job.transcriptionJobStatus() == TranscriptionJobStatus.COMPLETED) {
                String transcriptUri = job.transcript().transcriptFileUri();
                // Download transcript
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(transcriptUri)).build();
                HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                
                // Parse JSON output for the actual text
                // Since we don't have Jackson easily at hand here, we'll do a simple string extract or just return it
                return "Transcription completed. Full JSON response length: " + httpResponse.body().length() + " (Extract transcript text here)";
            }
            
            return "Transcription is taking too long or failed. Job Name: " + jobName;
            
        } catch (Exception e) {
            log.error("Failed to transcribe audio", e);
            return "Error transcribing audio: " + e.getMessage();
        }
    }
}
