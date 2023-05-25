package com.mms.repository;

import com.mms.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, String> {
    List<Code> findAllByOrderByNameAsc();
}
