package com.booquest.booquest_api.application.port.in.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.DeleteRecordResponse;

public interface DeleteRecordUseCase {
    DeleteRecordResponse deleteRecord(Long userId, Long recordId);
}
