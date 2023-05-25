package com.mms.repository;

import com.mms.model.ProvidenceFamily;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvidenceFamilyRepository extends JpaRepository<ProvidenceFamily, Long> {
    void deleteByUserSeqNo(Long userSeqNo);
}
