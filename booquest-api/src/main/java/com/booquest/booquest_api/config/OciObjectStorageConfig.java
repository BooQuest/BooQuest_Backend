package com.booquest.booquest_api.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OciObjectStorageConfig {
    @Value("${app.oci.access-key}") private String accessKey;
    @Value("${app.oci.secret-key}") private String secretKey;
    @Value("${app.oci.region}") private String region;
    @Value("${app.oci.namespace}") private String namespace;
    @Value("${app.oci.endpoint:}") private String endpoint;

    @Bean
    public AmazonS3 amazonS3() {
        String resolvedEndpoint = (endpoint == null || endpoint.isBlank())
                ? String.format("https://%s.compat.objectstorage.%s.oraclecloud.com", namespace, region)
                : endpoint;

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(resolvedEndpoint, region)
                )
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .build();
    }
}
