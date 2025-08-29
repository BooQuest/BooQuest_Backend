package com.booquest.booquest_api.adapter.out.storage.persistence;

import com.booquest.booquest_api.application.port.in.storage.ImageStoragePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Component
public class LocalImageStorageAdapter implements ImageStoragePort {
    private final String root;

    public LocalImageStorageAdapter(@Value("${app.upload.root:/var/lib/booquest/uploads}") String root) {
        this.root = root;
    }

    @Override
    public StoredObject storeProofImage(Long userId, Long stepId, MultipartFile file) {
        String ext = safeExt(file); // 구현
        String key = String.format("proofs/%d/%d/%s.%s", userId, stepId, UUID.randomUUID(), ext);

        Path dest = Paths.get(root, key).normalize();
        try {
            Files.createDirectories(dest.getParent());
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            log.error("save fail: dest={}, root={}, size={}, type={}", dest, root, file.getSize(), file.getContentType(), e);
            throw new UncheckedIOException("파일 저장 실패", e);
        }

        String publicUrl = "/uploads/" + key.replace("\\", "/");
        return new StoredObject(key, publicUrl, file.getSize(), file.getContentType());
    }

    @Override
    public void delete(String key) {
        try {
            Files.deleteIfExists(Paths.get(root, key));
        } catch (IOException ignored) {}
    }

    private String safeExt(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) contentType = "";

        // Content-Type 타입 기준 우선
        switch (contentType) {
            case "image/jpeg": return "jpg";
            case "image/png":  return "png";
            case "image/webp": return "webp";
        }

        // 파일명 확장자로 보조 판단
        String name = file.getOriginalFilename();
        if (name != null) {
            int i = name.lastIndexOf('.');
            if (i > -1) {
                String ext = name.substring(i + 1).toLowerCase();
                if (ext.matches("jpe?g|png|webp")) return ext.equals("jpeg") ? "jpg" : ext;
            }
        }
        throw new IllegalArgumentException("허용되지 않은 이미지 형식: " + contentType);
    }
}
