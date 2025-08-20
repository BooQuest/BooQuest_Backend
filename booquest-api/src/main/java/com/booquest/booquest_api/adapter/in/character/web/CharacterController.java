package com.booquest.booquest_api.adapter.in.character.web;

import com.booquest.booquest_api.adapter.in.character.web.dto.CharacterGrowthResponse;
import com.booquest.booquest_api.adapter.in.character.web.dto.UserCharacterResponse;
import com.booquest.booquest_api.application.port.in.character.GetCharacterUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/character")
public class CharacterController {

    private final GetCharacterUseCase getCharacterUseCase;

    @GetMapping
    public ApiResponse<UserCharacterResponse> getCharacter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        UserCharacterResponse response = getCharacterUseCase.getCharacterByUserId(userId);
        return ApiResponse.success("캐릭터 정보가 조회되었습니다.", response);
    }

    @GetMapping("/growth")
    public ApiResponse<CharacterGrowthResponse> getCharacterGrowth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        CharacterGrowthResponse response = getCharacterUseCase.getCharacterGrowthByUserId(userId);
        return ApiResponse.success("캐릭터 성장 정보가 조회되었습니다.", response);
    }
}