package com.booquest.booquest_api.adapter.in.bonus.dto;

import com.booquest.booquest_api.domain.bonus.enums.ProofType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProofRequest {
    @NotNull
    private ProofType proofType;
    @NotNull
    private String content; // link, text, imageUrl
}
