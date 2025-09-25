package com.booquest.booquest_api.adapter.in.chat.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class ChatSendResponse {
    private UUID conversationId;
    private String message; // assistant message
}
