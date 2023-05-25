package com.mms.repository;

import com.mms.model.AgeCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgeCountRepository extends JpaRepository<AgeCount, String> {

    AgeCount findByChurch(String church);
}
