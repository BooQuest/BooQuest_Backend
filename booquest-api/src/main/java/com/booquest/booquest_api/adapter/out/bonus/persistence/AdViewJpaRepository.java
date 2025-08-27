package com.booquest.booquest_api.adapter.out.bonus.persistence;

import com.booquest.booquest_api.domain.bonus.model.AdView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdViewJpaRepository extends JpaRepository<AdView, Long> {
}