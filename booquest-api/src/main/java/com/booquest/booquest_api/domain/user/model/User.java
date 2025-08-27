package com.booquest.booquest_api.domain.user.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Builder
@Getter
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private AuthProvider provider;

    @Column(name = "provider_user_id")
    private String providerUserId;

    private String email;

    private String nickname;

    private String socialNickname;
    private String profileImageUrl;

    public void updateProfile(String newSocialNickname, String newProfileImageUrl) {
        if (newSocialNickname != null && !newSocialNickname.isBlank()) {
            this.socialNickname = newSocialNickname;
        }
        if (newProfileImageUrl != null && !newProfileImageUrl.isBlank()) {
            this.profileImageUrl = newProfileImageUrl;
        }
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
