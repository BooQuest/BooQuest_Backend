package com.booquest.booquest_api.domain.chat.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_conversation")
@Builder
@Getter
public class ChatConversation extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private Set<ChatMessage> messages = new LinkedHashSet<>();

    public void updateTitleIfEmpty(String newTitle) {
        if (this.title == null || this.title.isBlank()) {
            this.title = newTitle;
        }
    }
}
