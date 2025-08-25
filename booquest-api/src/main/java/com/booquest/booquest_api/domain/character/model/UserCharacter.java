package com.booquest.booquest_api.domain.character.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.character.enums.CharacterType;
import com.booquest.booquest_api.domain.character.policy.LevelingPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_characters")
@Getter
@Builder
public class UserCharacter extends AuditableEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String name;

    private int level = 1;

    private int exp = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "character_type")
    private CharacterType characterType;

    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * EXP 증감(delta)을 적용하면서, 레벨업/다운을 정책에 따라 처리한다.
     * - exp는 '현재 레벨에서의 진행도'로 유지, 넘치면 레벨업하며 나머지로 세팅
     * - 레벨은 1 이하로 떨어지지 않음
     */
    public void applyExpDelta(int delta, LevelingPolicy policy) {
        if (delta == 0) return;

        int per = policy.expPerLevel(this);

        if (delta > 0) {
            int total = this.exp + delta;
            while (total >= per) {
                total -= per;
                this.level += 1;
            }
            this.exp = total;
        } else { // delta < 0
            int total = this.exp + delta; // delta는 음수
            while (total < 0 && this.level > 1) {
                this.level -= 1;
                total += per; // 한 레벨 내려가면 이전 레벨의 게이지를 채움
            }
            this.exp = Math.max(0, total); // 레벨1에서 더 깎여도 0 밑으로는 내려가지 않음
        }
    }

    public UserCharacter withCharacterType(CharacterType type) {
        this.characterType = type;
        return this;
    }
}
