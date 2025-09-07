package com.booquest.booquest_api.application.service.user;

import com.booquest.booquest_api.adapter.in.user.web.dto.DeleteAccountResponse;
import com.booquest.booquest_api.application.port.in.user.DeleteAccountUseCase;
import com.booquest.booquest_api.application.port.out.auth.SocialUnlinkPort;
import com.booquest.booquest_api.application.port.out.auth.TokenRepositoryPort;
import com.booquest.booquest_api.application.port.out.bonus.AdViewRepositoryPort;
import com.booquest.booquest_api.application.port.out.bonus.ProofRepositoryPort;
import com.booquest.booquest_api.application.port.out.character.CharacterCommandPort;
import com.booquest.booquest_api.application.port.out.income.IncomeRepositoryPort;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingCategoryRepository;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingProfileRepository;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.application.port.out.stepprogress.StepProgressRepositoryPort;
import com.booquest.booquest_api.application.port.out.user.UserCommandPort;
import com.booquest.booquest_api.application.port.out.user.UserQueryPort;
import com.booquest.booquest_api.application.port.out.usersidejob.UserSideJobRepositoryPort;
import com.booquest.booquest_api.application.port.out.userstat.UserStatRepositoryPort;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteAccountService implements DeleteAccountUseCase {
    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final SideJobRepositoryPort sideJobRepositoryPort;
    private final OnboardingProfileRepository onboardingProfileRepository;
    private final OnboardingCategoryRepository onboardingCategoryRepository;
    private final UserSideJobRepositoryPort userSideJobRepositoryPort;
    private final TokenRepositoryPort tokenRepositoryPort;
    private final IncomeRepositoryPort incomeRepositoryPort;
    private final ProofRepositoryPort proofRepositoryPort;
    private final AdViewRepositoryPort adViewRepositoryPort;
    private final MissionRepositoryPort missionRepositoryPort;
    private final MissionStepRepositoryPort missionStepRepositoryPort;
    private final StepProgressRepositoryPort stepProgressRepositoryPort;
    private final CharacterCommandPort characterCommandPort;
    private final UserStatRepositoryPort userStatRepositoryPort;
//    private final StoragePort StoragePort; // S3 등 외부파일 삭제용
    private final SocialUnlinkPort socialUnlinkPort;

    @Override
    public DeleteAccountResponse deleteUserCompletely(Long userId, @Nullable String providerAccessToken) {
        // 0) 존재/권한 체크
        var user = userQueryPort.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        // proofs가 외부 스토리지(S3)에 파일을 가지고 있으면 먼저 object 삭제
        // StoragePort.deleteAllForUser(userId);

        // 0) step_progress
        long deletedStepProgress = stepProgressRepositoryPort.deleteByUserId(userId);

        // 1) 미션 스텝 -> 미션
        long deletedMissionSteps = missionStepRepositoryPort.deleteByUserId(userId);
        long deletedMissions = missionRepositoryPort.deleteByUserId(userId);

        // 2) 사용자 연결 데이터
        long deletedProofs = proofRepositoryPort.deleteByUserId(userId);
        long deletedIncomes = incomeRepositoryPort.deleteByUserId(userId);
        long deletedAdViews = adViewRepositoryPort.deleteByUserId(userId);
        long deletedTokens = tokenRepositoryPort.deleteByUserId(userId);

        // 3) 부업 선택/추천
        long deletedUserSideJobs = userSideJobRepositoryPort.deleteByUserId(userId);
        long deletedSideJobs = sideJobRepositoryPort.deleteByUserId(userId);

        // 4) 온보딩
        long deletedOnbCategories = onboardingCategoryRepository.deleteByUserId(userId);
        long deletedOnbProfiles = onboardingProfileRepository.deleteByUserId(userId);

        // 5) 캐릭터/통계
        long deletedUserCharacters = characterCommandPort.deleteByUserId(userId);
        long deletedUserStats = userStatRepositoryPort.deleteByUserId(userId);

        // 6) 사용자 삭제
        boolean userDeleted = userCommandPort.deleteById(userId) > 0;

        // 7) 소셜 Unlink
        boolean socialUnlinked = socialUnlinkPort.unlink(user.getProvider(), user.getProviderUserId(), providerAccessToken);

        return DeleteAccountResponse.builder()
                .deletedUserSideJobs(deletedUserSideJobs)
                .deletedSideJobs(deletedSideJobs)
                .deletedTokens(deletedTokens)
                .deletedIncomes(deletedIncomes)
                .deletedProofs(deletedProofs)
                .deletedAdViews(deletedAdViews)
                .deletedMissions(deletedMissions)
                .deletedMissionSteps(deletedMissionSteps)
                .deletedOnboardingCategories(deletedOnbCategories)
                .deletedOnboardingProfiles(deletedOnbProfiles)
                .deletedStepProgress(deletedStepProgress)
                .deletedUserCharacters(deletedUserCharacters)
                .deletedUserStats(deletedUserStats)
                .userDeleted(userDeleted)
                .socialUnlinked(socialUnlinked)
                .deletedAt(Instant.now())
                .build();
    }
}
