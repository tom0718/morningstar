package com.mms.repository;

import com.mms.model.RentRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface RentRoomRepository extends JpaRepository<RentRoom, Long>, JpaSpecificationExecutor<RentRoom> {
    RentRoom findBySeqNo(Long seqNo);

    List<RentRoom> findByRentDateBetweenAndStatusNot(LocalDate startDate, LocalDate endDate, String status);

    List<RentRoom> findByRentDateAndRoomAndStatusNotOrderByStartTimeAsc(LocalDate rentDate, String room, String status);
}
