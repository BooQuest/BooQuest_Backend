package com.booquest.booquest_api.domain.record.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
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

    @Column(name = "image_object_key")
    private String imageObjectKey;

    @Column(name = "image_presigned_url", length = 1000)
    private String imagePresignedUrl;

    @Column(name = "image_presigned_expires_at")
    private Instant imagePresignedExpiresAt;

    @Column(name = "xp_granted", nullable = false)
    private boolean xpGranted = false;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateImageObjectKey(String imageObjectKey) {
        this.imageObjectKey = imageObjectKey;
    }

    public void markXpGranted() {
        this.xpGranted = true;
    }

    public boolean canGrantXp() {
        return !this.xpGranted;
    }

    public void refreshImagePresigned(String presignedUrl, Instant expiresAt) {
        this.imagePresignedUrl = presignedUrl;
        this.imagePresignedExpiresAt = expiresAt;
    }
}
