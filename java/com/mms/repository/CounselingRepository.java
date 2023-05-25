package com.mms.repository;

import com.mms.model.Counseling;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselingRepository extends JpaRepository<Counseling, Long> {
    Counseling findBySeqNo(Long seqNo);

    Page<Counseling> findByUserSeqNo(Pageable pageable, Long userSeqNo);

    void deleteBySeqNo(Long seqNo);
}
