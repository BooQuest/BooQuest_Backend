package com.booquest.booquest_api.application.port.in.bonus;

import com.booquest.booquest_api.adapter.in.bonus.dto.BonusResponse;
import com.booquest.booquest_api.adapter.in.bonus.dto.ProofRequest;

public interface ProofUseCase {
    BonusResponse submitProofAndGrantExp(Long userId, Long stepId, ProofRequest request);
}
