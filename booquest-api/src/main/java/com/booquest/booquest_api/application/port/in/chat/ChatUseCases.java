package com.booquest.booquest_api.application.port.in.chat;

import com.booquest.booquest_api.adapter.in.chat.web.dto.ChatListResponse;
import com.booquest.booquest_api.adapter.in.chat.web.dto.ChatMessageListResponse;
import com.booquest.booquest_api.adapter.in.chat.web.dto.ChatSendCommand;
import com.booquest.booquest_api.adapter.in.chat.web.dto.ChatSendResponse;
import java.time.YearMonth;
import java.util.UUID;

public interface ChatUseCases {
    ChatSendResponse sendMessage(Long userId, ChatSendCommand command);
    ChatListResponse getConversations(Long userId, YearMonth month);
    ChatMessageListResponse getConversation(Long userId, UUID conversationId);
}
