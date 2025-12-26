package com.booquest.booquest_api.application.port.in.storage;

import com.booquest.booquest_api.application.port.in.record.PresignedUploadResponse;

import java.io.InputStream;
import java.time.Duration;

public interface ImageStoragePort {
    /**
     * 업로드용 presigned PUT URL 생성
     */
    PresignedUploadResponse createPresignedPutUrl(String objectKey, String contentType, int expiresSec);

    /**
     * 조회용 presigned GET URL 생성
     */
    String createPresignedGetUrl(String objectKey, Duration expires);

    void uploadObject(String objectKey, InputStream inputStream, long contentLength, String contentType);

    void deleteObject(String objectKey);
}
