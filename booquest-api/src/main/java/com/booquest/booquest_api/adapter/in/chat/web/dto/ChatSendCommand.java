package com.booquest.booquest_api.adapter.in.chat.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSendCommand {
    private UUID conversationId;    // null이면 새로 생성
    private String message;         // user message
}
