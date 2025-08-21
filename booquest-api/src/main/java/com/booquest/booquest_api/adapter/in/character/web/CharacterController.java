package com.booquest.booquest_api.adapter.in.character.web;

import com.booquest.booquest_api.adapter.in.character.web.dto.CharacterGrowthResponse;
import com.booquest.booquest_api.adapter.in.character.web.dto.UserCharacterResponse;
import com.booquest.booquest_api.application.port.in.character.GetCharacterUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/character")
@Tag(name = "Character", description = "캐릭터 API")
public class CharacterController {

    private final GetCharacterUseCase getCharacterUseCase;

    @GetMapping
    @Operation(summary = "캐릭터 조회", description = "로그인한 사용자의 캐릭터 상세 정보를 조회합니다.")
    public ApiResponse<UserCharacterResponse> getCharacter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        UserCharacterResponse response = getCharacterUseCase.getCharacterByUserId(userId);
        return ApiResponse.success("캐릭터 정보가 조회되었습니다.", response);
    }

    @GetMapping("/growth")
    @Operation(summary = "캐릭터 성장률 조회", description = "로그인한 사용자의 캐릭터 성장률을 조회합니다.")
    public ApiResponse<CharacterGrowthResponse> getCharacterGrowth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        CharacterGrowthResponse response = getCharacterUseCase.getCharacterGrowthByUserId(userId);
        return ApiResponse.success("캐릭터 성장률이 조회되었습니다.", response);
    }
}