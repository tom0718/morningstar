package com.mms.repository;

import com.mms.model.JobUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobUserRepository extends JpaRepository<JobUser, Long> {
    List<JobUser> findByChurchOrderByJobAsc(String church);
}
