package com.booquest.booquest_api.application.port.out.sidejob;

import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SideJobRepository extends JpaRepository<SideJob, Long> {

}
