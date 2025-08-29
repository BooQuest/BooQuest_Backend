package com.booquest.booquest_api.application.port.in.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoragePort {
    record StoredObject(String key, String publicUrl, long size, String contentType) {}
    StoredObject storeProofImage(Long userId, Long stepId, MultipartFile file);
    void delete(String key); // 실패 시 롤백/정리용
}
