package com.booquest.booquest_api.application.port.in.bonus;

import com.booquest.booquest_api.adapter.in.bonus.dto.BonusResponse;
import com.booquest.booquest_api.adapter.in.bonus.dto.ProofRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProofUseCase {
    BonusResponse submitProofAndGrantExp(Long userId, Long stepId, ProofRequest request);
    BonusResponse submitImageProofAndGrantExp(Long userId, Long stepId, MultipartFile file);
}
