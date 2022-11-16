package com.used.lux.repository;

import com.used.lux.domain.AppraisalRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppraisalRequestLogRepository extends JpaRepository<AppraisalRequestLog, Long> {
    List<AppraisalRequestLog> findByUserIdOrderByModifiedAtDesc(Long id);
}
