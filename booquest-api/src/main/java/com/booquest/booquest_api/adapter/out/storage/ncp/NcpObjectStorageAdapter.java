package com.booquest.booquest_api.adapter.out.storage.ncp;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.booquest.booquest_api.application.port.in.record.PresignedUploadResponse;
import com.booquest.booquest_api.application.port.in.storage.ImageStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class NcpObjectStorageAdapter implements ImageStoragePort {

    private final AmazonS3 s3;
    @Value("${app.ncp.bucket.name}")
    private String bucket;

    @Override
    public PresignedUploadResponse createPresignedPutUrl(String objectKey, String contentType, int expiresSec) {
        Date expiration = new Date(System.currentTimeMillis() + expiresSec * 1000L);

        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        if (contentType != null)
            request.addRequestParameter("Content-Type", contentType);

        URL url = s3.generatePresignedUrl(request);
        return new PresignedUploadResponse(objectKey, url.toString(), expiresSec);
    }

    @Override
    public String createPresignedGetUrl(String objectKey, Duration expires) {
        Date expiration = new Date(System.currentTimeMillis() + expires.toMillis());
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = s3.generatePresignedUrl(request);
        return url.toString();
    }
}
