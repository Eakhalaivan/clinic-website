package com.healthcare.clinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
@ConditionalOnProperty(name = "storage.provider", havingValue = "s3")
public class AwsConfig {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Value("${aws.access-key:#{null}}")
    private String accessKey;

    @Value("${aws.secret-key:#{null}}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        if (accessKey != null && !accessKey.isEmpty() && secretKey != null && !secretKey.isEmpty()) {
            return S3Client.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();
        }
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        if (accessKey != null && !accessKey.isEmpty() && secretKey != null && !secretKey.isEmpty()) {
            return S3Presigner.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();
        }
        return S3Presigner.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
