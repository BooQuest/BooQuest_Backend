package com.booquest.booquest_api.application.port.out.chat;

import com.booquest.booquest_api.domain.chat.model.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ChatMessageRepositoryPort {
    ChatMessage save(ChatMessage message);
    List<ChatMessage> findByConversationIdOrderByIdAsc(UUID conversationId);
    long countUserMessagesForUserBetween(Long userId, String role, LocalDateTime startInclusive, LocalDateTime endExclusive);
}
