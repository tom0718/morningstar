package com.mms.service;

import com.mms.model.*;
import com.mms.repository.AccountRoleRepository;
import com.mms.repository.RentRoomRepository;
import com.mms.repository.RentTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RoomService {

    private Map<String, Object> map;
    private RentRoom rentRoom;
    private RentTime rentStartTime;
    private RentTime rentEndTime;
    private AccountRole accountRole;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate rentDate;

    private List<RentRoom> rentRoomList;

    private RentRoomRepository rentRoomRepository;
    private AccountRoleRepository accountRoleRepository;
    private RentTimeRepository rentTimeRepository;

    public RoomService(
            RentRoomRepository rentRoomRepository,
            AccountRoleRepository accountRoleRepository,
            RentTimeRepository rentTimeRepository
    ) {
        this.rentRoomRepository = rentRoomRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.rentTimeRepository = rentTimeRepository;
    }


    @Transactional
    public Map<String, Object> enrollRentRoom(SessionUser session, RentRoomDTO data) {

        map = new HashMap<>();

        try{
            rentRoom = new RentRoom();

            // TODO 동일한 시간대가 있는지 체크필요 - rentTime: startTime, endTime check
            if(ObjectUtils.isNotEmpty(data.getSeqNo())){
                rentRoom = rentRoomRepository.findBySeqNo(data.getSeqNo());

                // 수정이면? 시간대가 변경되었는지 체크하고, 변경되었으면 rentTime: startTime, endTime check

            }else{

                // 등록이면? rentTime: startTime, endTime check


                rentRoom.setRegDatetime(LocalDateTime.now());
                rentRoom.setRegister(session.getId());
            }

            if(session.getPart().equals("staff")){ // 부서장
                accountRole = new AccountRole();
                accountRole = accountRoleRepository.findBySeqNo(data.getDepartment());

                rentRoom.setDepartment(accountRole.getDepartmentId());
                rentRoom.setGender(accountRole.getMale() ? "M" : "F");
            }else{
                rentRoom.setDepartment(data.getDepartment());

                String gender = data.getMale() && data.getFemale() ? "MF" : (data.getMale() && data.getFemale().equals(false) ? "M" : (data.getMale().equals(false) && data.getFemale() ? "F" : ""));

                rentRoom.setGender(gender);
            }

            rentRoom.setRoom(data.getRoom());
            rentRoom.setRentDate(LocalDate.parse(data.getRentDate(), dateFormat));
            rentRoom.setStartTime(LocalTime.parse(data.getStartTime(), timeFormat));
            rentRoom.setEndTime(LocalTime.parse(data.getEndTime(), timeFormat));
            rentRoom.setPurpose(data.getPurpose());
            rentRoom.setStatus("active");

            long differentTime = ChronoUnit.MINUTES.between(rentRoom.getStartTime(), rentRoom.getEndTime());

            rentRoom.setDifferentTime((int)(differentTime/30));

            rentRoom = rentRoomRepository.save(rentRoom);

            LocalTime time1 = rentRoom.getStartTime();
            LocalTime time2 = rentRoom.getEndTime();

            Long roomSeqNo = rentRoom.getSeqNo();
            LocalDate rentDate = rentRoom.getRentDate();
            String roomName = rentRoom.getRoom();

            rentTimeRepository.updateTemp(session.getId(), roomSeqNo, roomName, rentDate, true);

            do{
                rentStartTime = new RentTime();
                rentStartTime.setRentRoomSeqNo(roomSeqNo);
                rentStartTime.setUser(session.getId());
                rentStartTime.setType("start");
                rentStartTime.setRoom(roomName);
                rentStartTime.setRentDate(rentDate);
                rentStartTime.setRentTime(time1);
                rentStartTime.setTemp(false);

                //log.info("startTime ===> {}", rentStartTime.toString());

                rentTimeRepository.save(rentStartTime);

                rentEndTime = new RentTime();
                rentEndTime.setRentRoomSeqNo(roomSeqNo);
                rentEndTime.setUser(session.getId());
                rentEndTime.setType("end");
                rentEndTime.setRoom(roomName);
                rentEndTime.setRentDate(rentDate);
                rentEndTime.setRentTime(time1.plusMinutes(30));
                rentEndTime.setTemp(false);

                //log.info("endTime ===> {}", rentEndTime.toString());

                rentTimeRepository.save(rentEndTime);

                time1 = time1.plusMinutes(30);

            }while(time1.isBefore(time2));


            rentTimeRepository.deleteByRentRoomSeqNoAndUserAndRoomAndRentDateAndTemp(roomSeqNo, session.getId(), roomName, rentDate, true);

            map.put("result", "success");

        }catch (Exception e){
            log.error("save error ======> {}", e.getMessage());
            map.put("result", "fail");
        }

        return map;
    }

    public Map<String, Object> getBookRoomList(LocalDate today) {
        map = new HashMap<>();

        // -1 ~ +2달 이내의 신청목록 가져오기
        startDate = today.minusMonths(1);
        endDate = today.plusMonths(2);

        rentRoomList = new ArrayList<>();
        rentRoomList = rentRoomRepository.findByRentDateBetweenAndStatusNot(startDate, endDate, "cancel");

        map.put("rentRoomList", rentRoomList);
        map.put("startDate", startDate.format(dateFormat));
        map.put("endDate", endDate.format(dateFormat));
        map.put("today", today.format(dateFormat));

        return map;
    }

    public Map<String, Object> getRoomCondition(String eventDate, Long seqNo) {
        map = new HashMap<>();

        String roomName = "";

        if(ObjectUtils.isNotEmpty(seqNo)){
            rentRoom = new RentRoom();
            rentRoom = rentRoomRepository.findBySeqNo(seqNo);

            roomName = rentRoom.getRoom();

            rentDate = rentRoom.getRentDate();

            map.put("rentRoom", rentRoom);
        }else{
            roomName = "S1";
            rentDate = LocalDate.parse(eventDate, dateFormat);
        }

        rentRoomList = new ArrayList<>();
        rentRoomList = rentRoomRepository.findByRentDateAndRoomAndStatusNotOrderByStartTimeAsc(rentDate, roomName, "cancel");

        map.put("rentDate", rentDate);
        map.put("rentRoomList", rentRoomList);


        return map;
    }
}
