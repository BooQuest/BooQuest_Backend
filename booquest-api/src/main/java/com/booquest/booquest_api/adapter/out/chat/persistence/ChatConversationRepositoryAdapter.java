package com.booquest.booquest_api.adapter.out.chat.persistence;

import com.booquest.booquest_api.application.port.out.chat.ChatConversationRepositoryPort;
import com.booquest.booquest_api.domain.chat.model.ChatConversation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatConversationRepositoryAdapter implements ChatConversationRepositoryPort {

    private final ChatConversationJpaRepository repository;

    @Override
    public ChatConversation save(ChatConversation conversation) {
        return repository.save(conversation);
    }

    @Override
    public Optional<ChatConversation> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ChatConversation> findByIdAndUserId(UUID id, Long userId) {
        return repository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<ChatConversation> findByUserIdOrderByUpdatedAtDesc(Long userId) {
        return repository.findByUserIdOrderByUpdatedAtDesc(userId);
    }

    @Override
    public List<ChatConversation> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end) {
        return repository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);
    }
}
