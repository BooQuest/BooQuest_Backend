package com.booquest.booquest_api.adapter.out.chat.persistence;

import com.booquest.booquest_api.domain.chat.model.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversation_IdOrderByIdAsc(UUID conversationId);
    long countByConversation_UserIdAndRoleAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(Long userId, String role, LocalDateTime startInclusive, LocalDateTime endExclusive);

//    List<ChatMessage> findByConversationIdOrderByIdAsc(UUID conversationId);
//
//    @Query("SELECT COUNT(cm) FROM ChatMessage cm " +
//           "JOIN cm.conversation c " +
//           "WHERE c.userId = :userId AND cm.role = :role " +
//           "AND cm.createdAt >= :startInclusive AND cm.createdAt < :endExclusive")
//    long countUserMessagesForUserBetween(@Param("userId") Long userId,
//                                        @Param("role") String role,
//                                        @Param("startInclusive") LocalDateTime startInclusive,
//                                        @Param("endExclusive") LocalDateTime endExclusive);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage cm WHERE cm.conversation.userId = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
