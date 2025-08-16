package com.booquest.booquest_api.adapter.out.sidejob.persistence;

import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class SideJobRepositoryAdapter implements SideJobRepositoryPort {

    private final SideJobRepository sideJobRepository;

    @Override
    public SideJob save(SideJob sideJob) {
        return sideJobRepository.save(sideJob);
    }

    @Override
    public int updateSelectedFalse(Long id) {
        return sideJobRepository.updateSelectedFalse(id);
    }
}