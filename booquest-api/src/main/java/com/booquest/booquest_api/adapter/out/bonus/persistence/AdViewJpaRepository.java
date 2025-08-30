package com.booquest.booquest_api.adapter.out.bonus.persistence;

import com.booquest.booquest_api.domain.bonus.model.AdView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdViewJpaRepository extends JpaRepository<AdView, Long> {
    long deleteByUserId(Long userId);
    
    /**
     * 특정 부퀘스트에 대한 완료된 광고 시청이 있는지 확인
     */
    boolean existsByStepIdAndUserIdAndIsCompletedTrue(Long stepId, Long userId);
}