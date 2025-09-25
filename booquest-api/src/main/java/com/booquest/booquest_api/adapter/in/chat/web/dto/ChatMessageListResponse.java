package com.booquest.booquest_api.adapter.in.chat.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class ChatMessageListResponse {
    private UUID conversationId;
    private String title;
    private List<ChatMessageItem> messages;
}
