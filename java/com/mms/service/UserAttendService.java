package com.mms.service;

import com.mms.model.*;
import com.mms.repository.DepartmentRepository;
import com.mms.repository.SearchUserRepository;
import com.mms.repository.UserAttendRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserAttendService {

    private Map<String, Object> map;
    private UserAttend userAttend;
    private SearchUser searchUser;
    private List<Predicate> predicates;
    private Long temp;
    private String[] genderArray;

    @PersistenceContext
    private EntityManager entityManager;

    private UserAttendRepository userAttendRepository;
    private SearchUserRepository searchUserRepository;
    private DepartmentRepository departmentRepository;

    @Autowired
    public UserAttendService(
            UserAttendRepository userAttendRepository,
            SearchUserRepository searchUserRepository,
            DepartmentRepository departmentRepository
    ){
        this.userAttendRepository = userAttendRepository;
        this.searchUserRepository = searchUserRepository;
        this.departmentRepository = departmentRepository;
    }


    public boolean checkAttendWorship(Long seqNo, LocalDate localDate, String type) {
        return userAttendRepository.existsByUserSeqNoAndAttendDateAndType(seqNo, localDate, type);
    }

    public void saveAttend(Long userSeqNo, Long department, String gender, String type, LocalDate attendDate, String attendType, String reason) {

        userAttend = new UserAttend();
        userAttend.setType(type);
        userAttend.setAttendType(attendType);
        userAttend.setUserSeqNo(userSeqNo);
        userAttend.setAttendDate(attendDate);
        userAttend.setAttendYear(attendDate.getYear());
        userAttend.setAttendMonth(attendDate.getMonth().getValue());
        userAttend.setReason(reason.equals("") ? null : reason);
        userAttend.setRegDatetime(LocalDateTime.now());
        userAttend.setDepartment(department);
        userAttend.setGender(gender);

        userAttendRepository.save(userAttend);
    }

    public Map<String, Object> enrollAttend(String secure, String register) {
        map = new HashMap<>();

        LocalDate localDate = LocalDate.now();
        int dayOfWeekNumber = localDate.getDayOfWeek().getValue();

        searchUser = new SearchUser();
        searchUser = searchUserRepository.findBySecure(secure);

        Boolean checkAttend = false;
        String type = "";
        if(dayOfWeekNumber == 3){
            type = "wednesday";
        }else if(dayOfWeekNumber == 7){
            type = "sunday";
        }

        checkAttend = this.checkAttendWorship(searchUser.getSeqNo(), localDate, type);

        if(!type.equals("") && checkAttend.equals(false)){
            this.saveAttend(searchUser.getSeqNo(), searchUser.getDepartmentSeqNo(), searchUser.getGender(), type, localDate, "mainChurch", "");
            checkAttend = true;
        }

        map.put("checkAttend", checkAttend);

        return map;
    }

    public Page<UserAttend> getAttendList(Pageable pageable, SessionUser sessionUser, String code, String name, Long department, String gender) {
        /*
        Specification<UserAttend> specification = new Specification<UserAttend>() {

            @Override
            public Predicate toPredicate(Root<UserAttend> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                predicates = new ArrayList<>();

                predicates.add(builder.equal(root.get("deleted"), false));

                if(sessionUser.getPart().equals("supreme")){
                    if(!code.equals("")){
                        predicates.add(builder.equal(root.get("code"), code));
                    }
                }else{
                    predicates.add(builder.equal(root.get("code"), sessionUser.getCode()));
                }

                if(sessionUser.getPart().equals("supreme") || sessionUser.getPart().equals("pastor")){

                    if(!department.equals(0l)){
                        predicates.add(builder.equal(root.get("departmentSeqNo"), department));
                    }

                    if (!gender.equals("")) {
                        String[] arr = gender.split(",");
                        predicates.add(builder.in(root.get("gender")).value(Arrays.asList(arr)));
                    }



                }else{ // TODO 목록 가져오는 수정필요(multiple 부서)

                    temp = 0l;

                    Map<Long, String[]> roleMap = new HashMap<>();

                    sessionUser.getRoleList().forEach(r -> {
                        String genderStr = r.getMale() ? "M" : "F";

                        if(r.getDepartmentId().equals(temp)){
                            genderArray = new String[2];
                            genderArray[0] = roleMap.get(r.getDepartmentId())[0];
                            genderArray[1] = genderStr;

                            roleMap.put(r.getDepartmentId(), genderArray);

                        }else{
                            roleMap.put(r.getDepartmentId(), new String[]{genderStr});
                        }
                        temp = r.getDepartmentId();
                    });

                    List<Predicate> predicates2 = new ArrayList<>();

                    roleMap.forEach((k,v) -> {

                        Predicate predicateK = builder.equal(root.get("departmentSeqNo"), k);
                        Predicate predicateV = builder.in(root.get("gender")).value(Arrays.asList(v));

                        Predicate predicateL = builder.and(predicateK, predicateV);

                        predicates2.add(predicateL);

                    });

                    //predicates.add(builder.or(p2)); // builder.or(p1,p2,p3......);

                    int length = predicates2.size();

                    if(length == 1){
                        predicates.add(predicates2.get(0));
                    }else if(length == 2){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1)));
                    }else if(length == 3){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2)));
                    }else if(length == 4){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3)));
                    }else if(length == 5){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4)));
                    }else if(length == 6){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5)));
                    }else if(length == 7){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6)));
                    }else if(length == 8){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7)));
                    }else if(length == 9){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8)));
                    }else if(length == 10){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8), predicates2.get(9)));
                    }else if(length == 11){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8), predicates2.get(9), predicates2.get(10)));
                    }else if(length == 12){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8), predicates2.get(9), predicates2.get(10), predicates2.get(11)));
                    }else if(length == 13){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8), predicates2.get(9), predicates2.get(10), predicates2.get(11), predicates2.get(12)));
                    }else if(length == 14){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8), predicates2.get(9), predicates2.get(10), predicates2.get(11), predicates2.get(12), predicates2.get(13)));
                    }else if(length == 15){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8), predicates2.get(9), predicates2.get(10), predicates2.get(11), predicates2.get(12), predicates2.get(13), predicates2.get(14)));
                    }else if(length == 16){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8), predicates2.get(9), predicates2.get(10), predicates2.get(11), predicates2.get(12), predicates2.get(13), predicates2.get(14), predicates2.get(15)));
                    }else if(length == 17){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8), predicates2.get(9), predicates2.get(10), predicates2.get(11), predicates2.get(12), predicates2.get(13), predicates2.get(14), predicates2.get(15), predicates2.get(16)));
                    }else if(length == 18){
                        predicates.add(builder.or(predicates2.get(0), predicates2.get(1), predicates2.get(2), predicates2.get(3), predicates2.get(4), predicates2.get(5), predicates2.get(6), predicates2.get(7), predicates2.get(8), predicates2.get(9), predicates2.get(10), predicates2.get(11), predicates2.get(12), predicates2.get(13), predicates2.get(14), predicates2.get(15), predicates2.get(16), predicates2.get(17)));
                    }
                }

                if(!name.equals("")){ // 본명 또는 개명 검색
                    predicates.add(builder.or(builder.like(root.get("realName"), "%" + name + "%"), builder.like(root.get("reName"), "%" + name + "%")));
                }

                query.where(predicates.toArray(new Predicate[] {}));

                return builder.and(predicates.toArray(new Predicate[] {}));
            }
        };

        return userAttendRepository.findAll(specification, pageable);
        */
        return null;
    }

    public List<Department> getDepartmentList() {
        return departmentRepository.findAllByOrderByArrayAsc();
    }

    public List<DepartmentCount> getDepartmentCountList(String code) {
        Query query = entityManager.createNativeQuery("call proc_get_department(?)", DepartmentCount.class);
        query.setParameter(1, code);
        return query.getResultList();
    }

    public String getWorshipType(Integer numberOfDay){

        String worshipType = "";
        switch (numberOfDay){
            case 3 : worshipType = "wednesday"; break;
            case 7 : worshipType = "sunday"; break;
            default: worshipType = "dawn";
        }

        return worshipType;
    }

    public Integer countByDepartmentAndAttendType(Long departmentSeqNo, String attendType, LocalDate attendDate) {
        return userAttendRepository.countByDepartmentAndTypeAndAttendDate(departmentSeqNo, attendType, attendDate);
    }
}
