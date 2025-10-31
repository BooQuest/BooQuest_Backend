package com.booquest.booquest_api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NcpS3Config {
    @Value("${app.ncp.access-key}") private String accessKey;
    @Value("${app.ncp.secret-key}") private String secretKey;
    @Value("${app.ncp.endpoint}")   private String endpoint;
    @Value("${app.ncp.region}")     private String region;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpoint, region)
                )
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
