package com.booquest.booquest_api.application.port.in.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.CreateRecordRequest;
import com.booquest.booquest_api.adapter.in.record.web.dto.DailyRecordResponse;

public interface CreateRecordUseCase {
    DailyRecordResponse createRecord(Long userId, CreateRecordRequest command);
}
