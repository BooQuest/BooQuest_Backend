package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.UpdateRecordRequest;
import com.booquest.booquest_api.adapter.in.record.web.dto.UpdateRecordResponse;
import com.booquest.booquest_api.application.port.in.character.UpdateCharacterExpUseCase;
import com.booquest.booquest_api.application.port.in.record.UpdateRecordUseCase;
import com.booquest.booquest_api.application.port.out.record.DailyRecordRepositoryPort;
import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import com.booquest.booquest_api.domain.record.model.DailyRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UpdateRecordService implements UpdateRecordUseCase {
    private final DailyRecordRepositoryPort dailyRecordRepository;
    private final UpdateCharacterExpUseCase updateCharacterExpUseCase;

    private static final int DAILY_RECORD_XP = 5;

    @Override
    public UpdateRecordResponse updateRecord(Long userId, UpdateRecordRequest request) {
        LocalDate today = LocalDate.now();

        Optional<DailyRecord> existingRecord = dailyRecordRepository.findByUserIdAndRecordDate(userId, today);

        DailyRecord record;
        boolean isNewRecord = false;

        if (existingRecord.isPresent()) {
            // 기존 기록 수정
            record = existingRecord.get();
            record.updateContent(request.getContent());
            if (request.getImageUrl() != null) {
                record.updateImageUrl(request.getImageUrl());
            }
        } else {
            // 새 기록 생성
            record = DailyRecord.builder()
                    .userId(userId)
                    .recordDate(today)
                    .content(request.getContent())
                    .imageUrl(request.getImageUrl())
                    .xpGranted(false)
                    .build();
            isNewRecord = true;
        }

        DailyRecord savedRecord = dailyRecordRepository.save(record);

        // XP 지급 (새 기록이고 아직 XP를 받지 않은 경우)
        int xpAmount = 0;
        if (isNewRecord && savedRecord.canGrantXp()) {
            UserCharacter character = updateCharacterExpUseCase.applyReward(userId, RewardType.DAILY_RECORD);
            savedRecord.markXpGranted();
            dailyRecordRepository.save(savedRecord);
            xpAmount = DAILY_RECORD_XP;
            log.info("Daily record XP granted: userId={}, xp={}", userId, xpAmount);
        }

        return null;
//        return new UpdateRecordResponse(
//                savedRecord.getId(),
//                savedRecord.getRecordDate().toString(),
//                savedRecord.getContent(),
//                savedRecord.getImageUrl(),
//                savedRecord.isXpGranted(),
//                xpAmount
//        );
    }

}
