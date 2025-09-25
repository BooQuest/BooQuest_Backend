package com.booquest.booquest_api.adapter.out.chat.persistence;

import com.booquest.booquest_api.application.port.out.chat.ChatMessageRepositoryPort;
import com.booquest.booquest_api.domain.chat.model.ChatMessage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageRepositoryAdapter implements ChatMessageRepositoryPort {

    private final ChatMessageJpaRepository repository;

    @Override
    public ChatMessage save(ChatMessage message) {
        return repository.save(message);
    }

    @Override
    public List<ChatMessage> findByConversationIdOrderByIdAsc(UUID conversationId) {
        return repository.findByConversation_IdOrderByIdAsc(conversationId);
    }
}
