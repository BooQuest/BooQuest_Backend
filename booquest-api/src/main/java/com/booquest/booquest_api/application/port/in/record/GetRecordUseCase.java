package com.booquest.booquest_api.application.port.in.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.DailyRecordResponse;

import java.time.LocalDate;

public interface GetRecordUseCase {
    DailyRecordResponse getRecordByDate(Long userId, LocalDate date);
}
