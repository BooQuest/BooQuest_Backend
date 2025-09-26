package com.booquest.booquest_api.adapter.in.chat.web;

import com.booquest.booquest_api.adapter.in.chat.web.dto.*;
import com.booquest.booquest_api.application.port.in.chat.ChatUseCases;
import com.booquest.booquest_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "챗봇 API")
public class ChatController {

    private final ChatUseCases chatUseCases;

    @PostMapping
    @Operation(summary = "챗봇 대화", description = "사용자의 메시지를 보내고 AI의 응답을 받습니다. <br/><br/>" +
            "- 첫 대화일 경우 conversationId는 null로 보냅니다. 이어지는 대화일 경우 conversationId를 보내야 합니다. <br/>" +
            "- 챗봇 대화는 사용자 당 하루 10회까지만 가능합니다. 10회 초과일 경우 conversationId는 null로 반환됩니다. <br/>" +
            "- 대화의 제목은 첫 메시지를 짧게 요약하여 생성합니다.")
    public ApiResponse<ChatSendResponse> send(@Valid @RequestBody ChatSendCommand request) {
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        Long userId = getUserId();
        ChatSendResponse response = chatUseCases.sendMessage(userId, request);
        String responseMessage = response.getConversationId()!=null ? "AI의 응답을 받았습니다." : "하루 챗봇 대화 사용량 10회 초과되었습니다.";
        return ApiResponse.success(responseMessage, response);
    }

    @GetMapping
    @Operation(summary = "대화 목록 조회", description = "월 단위로 대화 목록을 그룹핑하여 반환합니다. month=YYYY-MM 형식")
    public ApiResponse<ChatListResponse> getConversations(@RequestParam(value = "month", required = false) String monthStr) {
        Long userId = getUserId();
        YearMonth month = monthStr == null || monthStr.isBlank() ? null : YearMonth.parse(monthStr);
        ChatListResponse response = chatUseCases.getConversations(userId, month);
        return ApiResponse.success("대화 목록이 조회되었습니다.", response);
    }

    @GetMapping("/{conversationId}")
    @Operation(summary = "대화 조회", description = "특정 대화의 메시지 목록을 조회합니다.")
    public ApiResponse<ChatMessageListResponse> getConversation(@PathVariable("conversationId") UUID conversationId) {
        Long userId = getUserId();
        ChatMessageListResponse response = chatUseCases.getConversation(userId, conversationId);
        return ApiResponse.success("대화가 조회되었습니다.", response);
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }
}
