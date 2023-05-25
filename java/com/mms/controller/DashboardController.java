package com.mms.controller;

import com.mms.model.*;
import com.mms.repository.DepartmentRepository;
import com.mms.service.AccountService;
import com.mms.service.DashboardService;
import com.mms.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController extends FunctionUtil {

    private Map<String, Object> map;

    private SessionUser sessionUser;

    private AgeCount ageCount;

    private List<GenderCount> genderCountList;

    private List<DepartmentCount> departmentCountList;

    private List<Department> departmentList;
    private List<JobUser> jobList;
    private List<SpecialUnitUser> specialUnitList;
    private List<StarUser> starList;


    @Autowired
    private AccountService accountService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(org.apache.commons.lang3.ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        genderCountList = new ArrayList<>();

        // TODO staff - 부서의 인원, 남여구성   pastor - 전체 성별, 나이별, 부서별 통계 보여줌.
        if(sessionUser.getPart().equals("staff")){

            //genderCountList = dashboardService.getGenderCountListByDepartment(sessionUser.getCode(), sessionUser);

        }else{
            ageCount = new AgeCount();
            ageCount = dashboardService.getAgeCount(sessionUser.getCode());

            if(ObjectUtils.isEmpty(ageCount)) {
                ageCount = new AgeCount();
                ageCount.setBaby(0);
                ageCount.setTen(0);
                ageCount.setTwenty(0);
                ageCount.setThirty(0);
                ageCount.setForty(0);
                ageCount.setFifty(0);
                ageCount.setSixty(0);
                ageCount.setSeventy(0);
                ageCount.setEighty(0);
                ageCount.setNinety(0);
                ageCount.setHundred(0);
            }

            Integer total = ageCount.getBaby() + ageCount.getTen() + ageCount.getTwenty() + ageCount.getThirty() + ageCount.getForty() + ageCount.getFifty() + ageCount.getSixty() + ageCount.getSeventy() + ageCount.getEighty() + ageCount.getNinety() + ageCount.getHundred();



            genderCountList = dashboardService.getGenderCountListByChurch(sessionUser.getCode());

            departmentCountList = new ArrayList<>();
            departmentCountList = dashboardService.getDepartmentCountList(sessionUser.getCode());

            departmentList = new ArrayList<>();
            departmentList = departmentRepository.findAllByOrderByArrayAsc();

            jobList = new ArrayList<>();
            jobList = dashboardService.getJobUserList(sessionUser.getCode());

            starList = new ArrayList<>();
            starList = dashboardService.getStarUserList(sessionUser.getCode());

            specialUnitList = new ArrayList<>();
            specialUnitList = dashboardService.getSpecialUnitList(sessionUser.getCode());

            model.addAttribute("totalCount", total);
            model.addAttribute("ageCount", ageCount);
            model.addAttribute("departmentCountList", departmentCountList);
            model.addAttribute("departmentNameList", departmentList);

            model.addAttribute("jobList", jobList);
            model.addAttribute("starList", starList);
            model.addAttribute("specialUnitList", specialUnitList);

        }


        model.addAttribute("navi", "dashboard");
        model.addAttribute("genderList", genderCountList);
        model.addAttribute("jobItem", jobItem);
        model.addAttribute("specialUnitItem", specialUnitItem);
        model.addAttribute("starItem", starItem);

        return "/user/dashboard";

    }






}
