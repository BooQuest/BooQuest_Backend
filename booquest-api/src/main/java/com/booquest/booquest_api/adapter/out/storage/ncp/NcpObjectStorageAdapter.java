package com.booquest.booquest_api.adapter.out.storage.ncp;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.booquest.booquest_api.application.port.in.record.PresignedUploadResponse;
import com.booquest.booquest_api.application.port.in.storage.ImageStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class NcpObjectStorageAdapter implements ImageStoragePort {

    private final AmazonS3 s3;
    @Value("${app.ncp.bucket.name}")
    private String bucketName;

    @Override
    public PresignedUploadResponse createPresignedPutUrl(String objectKey, String contentType, int expiresSec) {
        Date expiration = new Date(System.currentTimeMillis() + expiresSec * 1000L);

        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
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
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = s3.generatePresignedUrl(request);
        return url.toString();
    }

    @Override
    public void uploadObject(String objectKey, InputStream inputStream, long contentLength, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        if (contentType != null) {
            metadata.setContentType(contentType);
        }

        try {
            s3.putObject(bucketName, objectKey, inputStream, metadata);
            log.info("Uploaded object to NCP S3: key={}", objectKey);
        } catch (Exception e) {
            log.error("Failed to upload object to NCP S3. key={}", objectKey, e);
            throw new RuntimeException("이미지 업로드에 실패했습니다.");
        }
    }

    @Override
    public void deleteObject(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            return;
        }
        try {
            s3.deleteObject(bucketName, objectKey);
            log.info("Deleted object from NCP: {}", objectKey);
        } catch (Exception e) {
            // 여기서 바로 예외 던져도 되고, 로깅만 하고 넘어가도 됨
            log.error("Failed to delete object from NCP: {}", objectKey, e);
            throw new RuntimeException("이미지 삭제에 실패했습니다.", e);
        }
    }
}
