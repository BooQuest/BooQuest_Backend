package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.DailyRecordResponse;
import com.booquest.booquest_api.application.port.in.character.UpdateCharacterExpUseCase;
import com.booquest.booquest_api.application.port.in.record.CreateRecordUseCase;
import com.booquest.booquest_api.application.port.in.storage.ImageStoragePort;
import com.booquest.booquest_api.application.port.out.record.DailyRecordRepositoryPort;
import com.booquest.booquest_api.common.exception.DailyRecordAlreadyExistsException;
import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.record.model.DailyRecord;
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
public class CreateRecordService implements CreateRecordUseCase {
    private final DailyRecordRepositoryPort dailyRecordRepository;
    private final ImageStoragePort imageStoragePort;
    private final UpdateCharacterExpUseCase updateCharacterExpUseCase;

    private static final int DAILY_RECORD_XP = 5;
    private static final Duration PRESIGNED_TTL = Duration.ofMinutes(10);

    @Override
    public DailyRecordResponse createRecord(Long userId, String content, MultipartFile file, LocalDate recordDate) {
        // 날짜가 null이면 오늘 날짜로 설정
        LocalDate targetDate = recordDate != null ? recordDate : LocalDate.now();

        // 0) 입력 검증
        boolean hasContent = content != null && !content.isBlank();
        boolean hasFile = file != null && !file.isEmpty() && !("".equals(file));
        if (!hasContent && !hasFile) {
            throw new IllegalArgumentException("내용 또는 이미지는 최소 하나 이상 있어야 합니다.");
        }

        // 1) 해당 날짜 기록 있는지 확인
        dailyRecordRepository.findByUserIdAndRecordDate(userId, targetDate)
                .ifPresent(r -> { throw DailyRecordAlreadyExistsException.with(r.getId(), r.getRecordDate()); });

        // 2) 파일 있으면 여기서 key 만들고 업로드
        String objectKey = null;
        String presignedUrl = null;
        Instant presignedExpiresAt = null;

        if (hasFile) {
            String datePart = targetDate.format(DateTimeFormatter.BASIC_ISO_DATE); // ex. 20251031
            String ext = getExt(file.getOriginalFilename());
            String key = String.format("records/%d/%s/%s%s",
                    userId,
                    datePart,
                    UUID.randomUUID(),
                    ext
            );
            try {
                imageStoragePort.uploadObject(
                        key,
                        file.getInputStream(),
                        file.getSize(),
                        file.getContentType()
                );
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
            }
            objectKey = key;

            presignedUrl = imageStoragePort.createPresignedGetUrl(objectKey, PRESIGNED_TTL);
            presignedExpiresAt = Instant.now().plus(PRESIGNED_TTL);
        }

        // 3) 엔티티 생성
        DailyRecord record = DailyRecord.builder()
                .userId(userId)
                .recordDate(targetDate)
                .content(content)
                .imageObjectKey(objectKey)
                .imagePresignedUrl(presignedUrl)
                .imagePresignedExpiresAt(presignedExpiresAt)
                .xpGranted(false)
                .build();

        int xpAmount = 0;
        if (record.canGrantXp()) {
            updateCharacterExpUseCase.applyReward(userId, RewardType.DAILY_RECORD);
            record.markXpGranted();
            xpAmount = DAILY_RECORD_XP;
        }

        DailyRecord saved = dailyRecordRepository.save(record);

        return new DailyRecordResponse(
                saved.getId(),
                saved.getRecordDate().toString(),
                saved.getContent(),
                saved.getImageObjectKey(),
                saved.getImagePresignedUrl(),
                saved.isXpGranted(),
                xpAmount
        );
    }

    private String getExt(String originalFilename) {
        if (originalFilename == null) return ".jpg";
        int idx = originalFilename.lastIndexOf('.');
        if (idx == -1) return ".jpg";
        return originalFilename.substring(idx);
    }
}
