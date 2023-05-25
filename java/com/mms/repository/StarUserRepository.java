package com.mms.repository;

import com.mms.model.StarUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarUserRepository extends JpaRepository<StarUser, Long> {
    List<StarUser> findByChurchOrderByStarAsc(String code);
}
