package com.booquest.booquest_api.domain.chat.model;

import com.booquest.booquest_api.common.entity.CreatedOnlyEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_message")
@Builder
@Getter
public class ChatMessage extends CreatedOnlyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private ChatConversation conversation;

    @Column(name = "role", nullable = false)
    private String role;    // user, assistant, system, tool

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "tokens")
    private Integer tokens;

    @Column(name = "latency_ms")
    private Integer latencyMs;
}
