package com.booquest.booquest_api.application.port.in.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.CalendarRecordResponse;

public interface GetCalendarRecordsUseCase {
    CalendarRecordResponse getCalendarRecords(Long userId, int year, int month);
}
