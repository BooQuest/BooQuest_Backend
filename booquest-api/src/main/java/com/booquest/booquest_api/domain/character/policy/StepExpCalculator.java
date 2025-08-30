package com.booquest.booquest_api.domain.character.policy;

import com.booquest.booquest_api.domain.character.enums.RewardType;
import org.springframework.stereotype.Component;

@Component
public class StepExpCalculator {
    
    /**
     * 부퀘스트 스테이지에서 받을 수 있는 총 경험치를 계산합니다.
     * 부퀘스트 완료(10Exp) + 인증하기/광고보기(10Exp) = 총 20Exp
     * 
     * @param rewardPolicy 경험치 정책
     * @return 부퀘스트 스테이지의 총 경험치
     */
    public int calculateTotalStepExp(CharacterRewardPolicy rewardPolicy) {
        int stepCompletionExp = rewardPolicy.expDeltaFor(RewardType.STEP_COMPLETED);
        int bonusExp = rewardPolicy.expDeltaFor(RewardType.PROOF_VERIFIED); // 또는 AD_WATCHED와 동일
        
        return stepCompletionExp + bonusExp;
    }
} 