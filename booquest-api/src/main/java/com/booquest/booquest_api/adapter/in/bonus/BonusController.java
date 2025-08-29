package com.booquest.booquest_api.adapter.in.bonus;

import com.booquest.booquest_api.adapter.in.bonus.dto.*;
import com.booquest.booquest_api.application.port.in.bonus.AdUseCase;
import com.booquest.booquest_api.application.port.in.bonus.ProofUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bonus")
@Tag(name = "Bonus", description = "추가 경험치 지급 API")
public class BonusController {
    private final ProofUseCase proofUseCase;
    private final AdUseCase adUseCase;

    @PostMapping("/{stepId}/proof")
    @Operation(summary = "링크/텍스트 인증하기", description = "부퀘스트 완료 후 링크/텍스트로 인증하여 추가 경험치를 받습니다. <br/><br/>" +
            "proofType: LINK, TEXT <br/><br/>" +
            "data.status 값 <br/>" +
            "- granted: 인증을 완료하고 추가 경험치를 지급 받았습니다. <br/>" +
            "- not-completed: 먼저 해당 스텝을 완료해주세요. <br/>" +
            "- blocked-by-ad: 해당 퀘스트는 이미 광고 보상으로 처리되어 인증 보상 지급이 불가합니다. <br/>" +
            "- already-verified: 이미 인증된 퀘스트입니다.")
    public ApiResponse<BonusResponse> proof(@PathVariable Long stepId, @Valid @RequestBody ProofRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        BonusResponse response = proofUseCase.submitProofAndGrantExp(userId, stepId, request);

        String message = switch (response.status()) {
            case GRANTED            -> "인증을 완료하고 추가 경험치를 지급 받았습니다.";
            case NOT_COMPLETED      -> "먼저 해당 스텝을 완료해주세요.";
            case BLOCKED_BY_AD      -> "해당 퀘스트는 이미 광고 보상으로 처리되어 인증 보상 지급이 불가합니다.";
            case ALREADY_VERIFIED   -> "이미 인증된 퀘스트입니다.";
            default                 -> "처리되었습니다.";
        };

        return ApiResponse.success(message, response);
    }

    @PostMapping(path = "/{stepId}/proof/image", consumes = "multipart/form-data")
    @Operation(summary = "사진 인증하기", description = "이미지 1장을 업로드하여 인증하고 추가 경험치를 받습니다. <br/>" +
            "이미지 크기 10MB 이하")
    public ApiResponse<BonusResponse> proofImage(@PathVariable Long stepId, @RequestPart("file") MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        BonusResponse response = proofUseCase.submitImageProofAndGrantExp(userId, stepId, file);

        String message = switch (response.status()) {
            case GRANTED          -> "인증을 완료하고 추가 경험치를 지급 받았습니다.";
            case NOT_COMPLETED    -> "먼저 해당 스텝을 완료해주세요.";
            case BLOCKED_BY_AD    -> "해당 퀘스트는 이미 광고 보상으로 처리되어 인증 보상 지급이 불가합니다.";
            case ALREADY_VERIFIED -> "이미 인증된 퀘스트입니다.";
            default               -> "처리되었습니다.";
        };
        return ApiResponse.success(message, response);
    }

    @PostMapping("/{stepId}/ad")
    @Operation(summary = "광고보기", description = "부퀘스트 완료 후 광고를 시청하여 추가 경험치를 받습니다. <br/><br/>" +
            "data.status 값 <br/>" +
            "- granted: 광고를 시청하고 추가 경험치를 지급 받았습니다. <br/>" +
            "- not-completed: 먼저 해당 스텝을 완료해주세요. <br/>" +
            "- blocked-by-proof: 해당 퀘스트는 이미 인증 보상으로 처리되어 광고 보상 지급이 불가합니다. <br/>" +
            "- already-watched: 이미 광고 보상을 받은 퀘스트입니다.")
    public ApiResponse<BonusResponse> watchAd(@PathVariable Long stepId, @Valid @RequestBody AdRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        BonusResponse response = adUseCase.watchAdAndGrantExp(userId, stepId, request);

        String message = switch (response.status()) {
            case GRANTED            -> "광고를 시청하고 추가 경험치를 지급 받았습니다.";
            case NOT_COMPLETED      -> "먼저 해당 스텝을 완료해주세요.";
            case BLOCKED_BY_PROOF   -> "해당 퀘스트는 이미 인증 보상으로 처리되어 광고 보상 지급이 불가합니다.";
            case ALREADY_WATCHED    -> "이미 광고 보상을 받은 퀘스트입니다.";
            default                 -> "처리되었습니다.";
        };

        return ApiResponse.success(message, response);
    }
}
