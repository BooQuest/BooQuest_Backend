package com.booquest.booquest_api.adapter.out.chat.persistence;

import com.booquest.booquest_api.domain.chat.model.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversation_IdOrderByIdAsc(UUID conversationId);
    long countByConversation_UserIdAndRoleAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(Long userId, String role, LocalDateTime startInclusive, LocalDateTime endExclusive);
}
