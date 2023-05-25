package com.mms.repository;

import com.mms.model.RentTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Transactional
public interface RentTimeRepository extends JpaRepository<RentTime, Long> {

    @Modifying
    @Query("update rentTime t set t.temp = :temp where t.room = :room and t.rentDate = :rentDate and t.user = :register and t.rentRoomSeqNo = :seqNo")
    void updateTemp(@Param("register") String register, @Param("seqNo")Long seqNo, @Param("room")String room, @Param("rentDate")LocalDate rentDate, @Param("temp")boolean temp);

    void deleteByRentRoomSeqNoAndUserAndRoomAndRentDateAndTemp(Long rentRoomSeqNo, String user, String room, LocalDate rentDate, boolean temp);
}
