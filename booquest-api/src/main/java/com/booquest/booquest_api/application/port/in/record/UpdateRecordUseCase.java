package com.booquest.booquest_api.application.port.in.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.UpdateRecordRequest;
import com.booquest.booquest_api.adapter.in.record.web.dto.UpdateRecordResponse;

public interface UpdateRecordUseCase {
    UpdateRecordResponse updateRecord(Long userId, Long recordId, UpdateRecordRequest request);
}
