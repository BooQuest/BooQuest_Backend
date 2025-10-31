package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.UpdateRecordRequest;
import com.booquest.booquest_api.adapter.in.record.web.dto.UpdateRecordResponse;
import com.booquest.booquest_api.application.port.in.record.UpdateRecordUseCase;
import com.booquest.booquest_api.application.port.in.storage.ImageStoragePort;
import com.booquest.booquest_api.application.port.out.record.DailyRecordRepositoryPort;
import com.booquest.booquest_api.domain.record.model.DailyRecord;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UpdateRecordService implements UpdateRecordUseCase {
    private final DailyRecordRepositoryPort dailyRecordRepositoryPort;
    private final ImageStoragePort imageStoragePort;

    private static final Duration PRESIGNED_TTL = Duration.ofMinutes(10);

    private static final int DAILY_RECORD_XP = 5;

    @Override
    public UpdateRecordResponse updateRecord(Long userId, Long recordId, UpdateRecordRequest request) {
        String content = request.getContent();
        MultipartFile file = request.getFile();
        Boolean removeImage = request.getRemoveImage();

        DailyRecord record = dailyRecordRepositoryPort.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Record not found: " + recordId));

        // 1) 글 수정
        if (request != null && content != null) {
            record.updateContent(content);
        }

        // 2) 이미지 지우라는 신호가 온 경우
        boolean wantRemoveImage = request != null && Boolean.TRUE.equals(removeImage);
        if (wantRemoveImage) {
            if (hasImage(record)) {
                imageStoragePort.deleteObject(record.getImageObjectKey());
            }
            record.updateImageObjectKey(null);
            record.refreshImagePresigned(null, null);
        }

        // 3) 새 파일이 올라온 경우 -> 새 파일로 수정
        if (file != null && !file.isEmpty() && !wantRemoveImage) {

            // 기존 이미지 있으면 한 번 더 삭제 (위에서 안 지웠을 수도 있으니까)
            if (hasImage(record)) {
                imageStoragePort.deleteObject(record.getImageObjectKey());
            }

            // 기존 날짜 그대로 쓰기
            LocalDate date = record.getRecordDate();
            String datePart = date.format(DateTimeFormatter.BASIC_ISO_DATE);
            String ext = getExt(file.getOriginalFilename());
            String newObjectKey = String.format("records/%d/%s/%s%s",
                    userId,
                    datePart,
                    UUID.randomUUID(),
                    ext
            );

            try {
                imageStoragePort.uploadObject(
                        newObjectKey,
                        file.getInputStream(),
                        file.getSize(),
                        file.getContentType()
                );
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
            }

            String presigned = imageStoragePort.createPresignedGetUrl(newObjectKey, PRESIGNED_TTL);
            Instant expiresAt = Instant.now().plus(PRESIGNED_TTL);

            record.updateImageObjectKey(newObjectKey);
            record.refreshImagePresigned(presigned, expiresAt);
        }

        DailyRecord saved = dailyRecordRepositoryPort.save(record);

        return new UpdateRecordResponse(saved.getId());
    }

    private boolean hasImage(DailyRecord record) {
        return record.getImageObjectKey() != null && !record.getImageObjectKey().isBlank();
    }

    private String getExt(String originalFilename) {
        if (originalFilename == null) return ".jpg";
        int idx = originalFilename.lastIndexOf('.');
        if (idx == -1) return ".jpg";
        return originalFilename.substring(idx);
    }
}
