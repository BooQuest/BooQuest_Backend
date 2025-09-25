package com.booquest.booquest_api.adapter.out.chat.persistence;

import com.booquest.booquest_api.domain.chat.model.ChatConversation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatConversationJpaRepository extends JpaRepository<ChatConversation, UUID> {
    Optional<ChatConversation> findById(UUID id);
    Optional<ChatConversation> findByIdAndUserId(UUID id, Long userId);
    List<ChatConversation> findByUserIdOrderByUpdatedAtDesc(Long userId);
    List<ChatConversation> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, LocalDateTime start, LocalDateTime end);
}
