package com.mms.repository;

import com.mms.model.SpecialUnitUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialUnitUserRepository extends JpaRepository<SpecialUnitUser,Long> {
    List<SpecialUnitUser> findByChurchOrderByUnitAsc(String church);
}
