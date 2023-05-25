package com.mms.repository;

import com.mms.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    void deleteByUserSeqNo(Long userSeqNo);
}
