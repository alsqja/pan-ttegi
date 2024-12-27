package com.example.panttegi.config;

import com.example.panttegi.util.AwsParameterStoreUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@PropertySource("classpath:application.properties")
public class AwsConfig {

    @Bean
    public String awsAccessKey() {
        return AwsParameterStoreUtil.getParameterValue("/dev/s3-access-key", true);
    }

    @Bean
    public String awsSecretKey() {
        return AwsParameterStoreUtil.getParameterValue("/dev/s3-secret-key", true);
    }

    @Bean
    public S3Client s3Client() {
        String accessKey = awsAccessKey();
        String secretKey = awsSecretKey();
        String region = "ap-northeast-2";

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .build();
    }
}
