package com.booquest.booquest_api.application.port.in.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.DailyRecordResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface CreateRecordUseCase {
    DailyRecordResponse createRecord(Long userId, String content, MultipartFile file, LocalDate recordDate);
}
