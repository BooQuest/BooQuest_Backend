package com.booquest.booquest_api.application.port.in.user;

import com.booquest.booquest_api.adapter.in.user.web.dto.MyActivitiesSummaryResponse;

public interface GetMyActivitiesSummaryUseCase {
    MyActivitiesSummaryResponse getSummary(Long userId);
}
