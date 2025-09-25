package com.booquest.booquest_api.adapter.in.chat.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class ChatListItem {
    private UUID conversationId;
    private String title;
    private LocalDateTime createdAt;
}
