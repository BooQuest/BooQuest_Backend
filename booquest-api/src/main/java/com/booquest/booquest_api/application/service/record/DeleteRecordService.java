package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.DeleteRecordResponse;
import com.booquest.booquest_api.adapter.out.record.persistence.DailyRecordRepository;
import com.booquest.booquest_api.application.port.in.character.UpdateCharacterExpUseCase;
import com.booquest.booquest_api.application.port.in.record.DeleteRecordUseCase;
import com.booquest.booquest_api.application.port.in.storage.ImageStoragePort;
import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.record.model.DailyRecord;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeleteRecordService implements DeleteRecordUseCase {
    private final DailyRecordRepository dailyRecordRepository;
    private final ImageStoragePort imageStoragePort;
    private final UpdateCharacterExpUseCase updateCharacterExpUseCase;

    @Override
    public DeleteRecordResponse deleteRecord(Long userId, Long recordId) {
        DailyRecord dailyRecord = dailyRecordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Record not found: " + recordId));

        // 1) 먼저 S3/NCP에서 이미지 삭제
        String objectKey = dailyRecord.getImageObjectKey();
        if (objectKey != null && !objectKey.isBlank()) {
            try {
                imageStoragePort.deleteObject(objectKey);
            } catch (Exception e) {
                log.error("Failed to delete object from NCP. objectKey={}, recordId={}", objectKey, recordId, e);
                throw new RuntimeException("이미지 삭제에 실패했습니다. 기록은 삭제되지 않았습니다.", e);
            }
        }

        // 2) 경험치 지급된 적 있으면 회수
        if (dailyRecord.isXpGranted()) {
            updateCharacterExpUseCase.revertReward(userId, RewardType.DAILY_RECORD);
        }

        // 3) DB에서 삭제
        dailyRecordRepository.delete(dailyRecord);

        return DeleteRecordResponse.builder()
                .id(recordId)
                .recordDate(dailyRecord.getRecordDate().toString())
                .build();
    }
}
