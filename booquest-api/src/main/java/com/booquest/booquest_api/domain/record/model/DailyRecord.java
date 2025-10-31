package com.booquest.booquest_api.domain.record.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_records")
@Builder
@Getter
public class DailyRecord extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "xp_granted", nullable = false)
    private boolean xpGranted = false;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void markXpGranted() {
        this.xpGranted = true;
    }

    public boolean canGrantXp() {
        return !this.xpGranted;
    }
}
