package com.booquest.booquest_api.application.port.out.chat;

import com.booquest.booquest_api.domain.chat.model.ChatConversation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatConversationRepositoryPort {
    ChatConversation save(ChatConversation conversation);
    Optional<ChatConversation> findById(UUID id);
    Optional<ChatConversation> findByIdAndUserId(UUID id, Long userId);
    List<ChatConversation> findByUserIdOrderByUpdatedAtDesc(Long userId);
    List<ChatConversation> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
