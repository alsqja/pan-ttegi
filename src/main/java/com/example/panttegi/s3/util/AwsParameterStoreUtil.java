package com.example.panttegi.s3.util;

import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

public class AwsParameterStoreUtil {

    public static String getParameterValue(String parameterName, boolean decrypt) {
        try (SsmClient ssmClient = SsmClient.create()) {
            GetParameterRequest request = GetParameterRequest.builder()
                    .name(parameterName)
                    .withDecryption(decrypt)
                    .build();

            GetParameterResponse response = ssmClient.getParameter(request);
            return response.parameter().value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve parameter: " + parameterName, e);
        }
    }

    public static void main(String[] args) {
        String accessKey = AwsParameterStoreUtil.getParameterValue("/dev/s3-access-key", true);
        String secretKey = AwsParameterStoreUtil.getParameterValue("/dev/s3-secret-key", true);

        System.out.println("Access Key: " + accessKey);
        System.out.println("Secret Key: " + secretKey);
    }

}
