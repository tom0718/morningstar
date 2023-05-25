package com.mms.repository;

import com.mms.model.UserAttend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;

public interface UserAttendRepository extends JpaRepository<UserAttend, Long>, JpaSpecificationExecutor<UserAttend> {
    boolean existsByUserSeqNoAndAttendDateAndType(Long userSeqNo, LocalDate attendDate, String type);

    Integer countByDepartmentAndAttendType(Long department, String attendType);

    Integer countByDepartmentAndTypeAndAttendDate(Long department, String attendType, LocalDate attendDate);
}
