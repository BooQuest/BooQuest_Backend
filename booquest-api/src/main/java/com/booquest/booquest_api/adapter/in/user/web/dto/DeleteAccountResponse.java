package com.booquest.booquest_api.adapter.in.user.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder(toBuilder = true)
public class DeleteAccountResponse {
    private long deletedUserSideJobs;
    private long deletedSideJobs;
    private long deletedTokens;
    private long deletedIncomes;
    private long deletedProofs;
    private long deletedAdViews;
    private long deletedMissions;
    private long deletedMissionSteps;
    private long deletedOnboardingCategories;
    private long deletedOnboardingProfiles;
    private long deletedStepProgress;
    private long deletedUserCharacters;
    private long deletedUserStats;
    private long deletedChatMessages;
    private long deletedChatConversations;
    private boolean userDeleted;
    private boolean socialUnlinked;
    private Instant deletedAt;
}
