package com.example.menuservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;


@Configuration
public class AwsConfig {

    @Value("${aws.region}")
    private String region;

    // S3 자격 증명
    @Value("${aws.s3.accessKeyId}")
    private String s3AccessKey;

    @Value("${aws.s3.secretAccessKey}")
    private String s3SecretKey;

    // SQS 자격 증명
    @Value("${aws.sqs.accessKeyId}")
    private String sqsAccessKey;

    @Value("${aws.sqs.secretAccessKey}")
    private String sqsSecretKey;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(s3AccessKey, s3SecretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public SqsClient sqsClient() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(sqsAccessKey, sqsSecretKey);
        return SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}