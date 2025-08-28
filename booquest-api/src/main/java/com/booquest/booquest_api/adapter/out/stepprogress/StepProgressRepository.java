package com.booquest.booquest_api.adapter.out.stepprogress;

import com.booquest.booquest_api.domain.missionstep.model.StepProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepProgressRepository extends JpaRepository<StepProgress, Long> {
    long deleteByUserId(Long userId);
}
