package com.mms.repository;

import com.mms.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {
    void deleteByUser(Long user);
}
