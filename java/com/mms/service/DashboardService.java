package com.mms.service;

import com.mms.model.*;
import com.mms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class DashboardService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AgeGenderRepository ageGenderRepository;

    @Autowired
    private AgeCountRepository ageCountRepository;

    @Autowired
    private JobUserRepository jobUserRepository;

    @Autowired
    private StarUserRepository starUserRepository;

    @Autowired
    private SpecialUnitUserRepository specialUnitUserRepository;


    public AgeCount getAgeCount(String code) {
        return ageCountRepository.findByChurch(code);
    }

    public List<GenderCount> getGenderCountListByChurch(String code) {
        Query query = entityManager.createNativeQuery("call proc_get_gender(?)", GenderCount.class);
        query.setParameter(1, code);
        return query.getResultList();
    }

    /*
    public List<GenderCount> getGenderCountListByDepartment(String code, SessionUser sessionUser) {
        Query query = entityManager.createNativeQuery("call proc_get_gender_by_department(?,?)", GenderCount.class);
        query.setParameter(1, code);
        query.setParameter(2, department);
        return query.getResultList();
    }
    */

    public List<DepartmentCount> getDepartmentCountList(String code) {
        Query query = entityManager.createNativeQuery("call proc_get_department(?)", DepartmentCount.class);
        query.setParameter(1, code);
        return query.getResultList();
    }

    public List<JobUser> getJobUserList(String code) {
        return jobUserRepository.findByChurchOrderByJobAsc(code);
    }

    public List<StarUser> getStarUserList(String code) {
        return starUserRepository.findByChurchOrderByStarAsc(code);
    }

    public List<SpecialUnitUser> getSpecialUnitList(String code) {
        return specialUnitUserRepository.findByChurchOrderByUnitAsc(code);
    }
}
