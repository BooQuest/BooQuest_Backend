package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.CreateRecordRequest;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreateRecordService implements CreateRecordUseCase {
    private final DailyRecordRepositoryPort dailyRecordRepository;
    private final ImageStoragePort imageStoragePort;
    private final UpdateCharacterExpUseCase updateCharacterExpUseCase;

    private static final int DAILY_RECORD_XP = 5;

    @Override
    public DailyRecordResponse createRecord(Long userId, CreateRecordRequest request) {
        LocalDate today = LocalDate.now();

        // 0) 입력 검증: 내용/이미지 모두 없으면 거절
        if ((request.getContent() == null || request.getContent().isBlank())
                && (request.getObjectKey() == null || request.getObjectKey().isBlank())) {
            throw new IllegalArgumentException("내용 또는 이미지는 최소 하나 이상 있어야 합니다.");
        }

        // 1) 오늘 기록 존재 확인
        Optional<DailyRecord> existingRecord = dailyRecordRepository.findByUserIdAndRecordDate(userId, today);
        if (existingRecord.isPresent()) {
            DailyRecord existing = existingRecord.get();
            throw DailyRecordAlreadyExistsException.with(existing.getId(), existing.getRecordDate());
        }

        // 2) 이미지 presigned GET URL 변환 (있을 경우만)
        String imageUrl = null;
        if (request.getObjectKey() != null && !request.getObjectKey().isBlank()) {
            imageUrl = imageStoragePort.createPresignedGetUrl(request.getObjectKey(), Duration.ofHours(1));
        }

        // 2) 새 레코드 구성 (xpGranted는 일단 false)
        DailyRecord record = DailyRecord.builder()
                .userId(userId)
                .recordDate(today)
                .content(request.getContent())
                .imageUrl(imageUrl)
                .xpGranted(false)
                .build();

        int xpAmount = 0;

        try {
            // 3) 저장 + XP 지급(최초 1회만)
            // canGrantXp()가 true일 때만 지급하고, 지급 후 flag 세팅
            if (record.canGrantXp()) {
                updateCharacterExpUseCase.applyReward(userId, RewardType.DAILY_RECORD);
                record.markXpGranted(); // true로 업데이트
                xpAmount = DAILY_RECORD_XP;
                log.info("Daily record XP granted: userId={}, xp={}", userId, xpAmount);
            }

            DailyRecord saved = dailyRecordRepository.save(record);

            return new DailyRecordResponse(
                    saved.getId(),
                    saved.getRecordDate().toString(),
                    saved.getContent(),
                    saved.getImageUrl(),
                    saved.isXpGranted(),
                    xpAmount
            );

        } catch (DataIntegrityViolationException e) {
            // 4) 동시성(유니크 제약) 충돌 -> 동일한 409로 변환
            Optional<DailyRecord> exists = dailyRecordRepository.findByUserIdAndRecordDate(userId, today);
            throw DailyRecordAlreadyExistsException.with(exists.map(DailyRecord::getId).orElse(null), today);
        }
    }
}
