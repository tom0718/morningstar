package com.mms.controller;

import com.mms.model.*;
import com.mms.repository.CodeRepository;
import com.mms.service.UserAttendService;
import com.mms.util.FunctionUtil;
import com.mms.util.PageWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/attend")
public class AttendController extends FunctionUtil {

    private StringBuffer url;
    private SessionUser sessionUser;
    private PageWrapper<UserAttend> attendPage;
    private List<Department> departmentList;
    private List<Code> codeList;
    private List<DepartmentCount> departmentCountList;
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private LocalDate inputDate;

    private UserAttendService attendService;
    private CodeRepository codeRepository;

    @Autowired
    public AttendController(
            UserAttendService attendService,
            CodeRepository codeRepository
    ) {
        this.attendService = attendService;
        this.codeRepository = codeRepository;
    }

    @RequestMapping("/list")
    public String list(
            //@PageableDefault(sort = { "seqNo" }, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @RequestParam(value = "code", defaultValue = "") String code,
            @RequestParam(value = "attendType", defaultValue = "") String attendType,
            //@RequestParam(value = "name", defaultValue = "") String name,
            //@RequestParam(value = "department", defaultValue = "0") Long department,
            //@RequestParam(value = "gender", defaultValue = "M,F") String gender,
            @RequestParam(value = "attendDate", defaultValue = "") String attendDate,
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        url = new StringBuffer();

        url.append("/attend/list?1=1");

        if(sessionUser.getPart().equals("supreme")){
            code = sessionUser.getCode();
            if(!code.equals("")){
                url.append("&code=");
                url.append(code);
            }
        }

        if(!attendDate.equals("")){
            url.append("&attendDate=");
            url.append(attendDate);
        }

        if(!attendType.equals("")){
            url.append("&attendType=");
            url.append(attendType);
        }

        /*
        if(!department.equals(0l)){
            url.append("&department=");
            url.append(department);
        }

        if(!gender.equals("")){
            url.append("&gender=");
            url.append(gender);
        }


        if(!name.equals("")){
            url.append("&name=");
            url.append(name);
        }
        */

        //departmentList = new ArrayList<>();
        //departmentList = attendService.getDepartmentList();

        departmentCountList = new ArrayList<>();
        departmentCountList = attendService.getDepartmentCountList(sessionUser.getCode());

        if(sessionUser.getPart().equals("supreme")){
            codeList = new ArrayList<>();
            codeList = codeRepository.findAllByOrderByNameAsc();

            model.addAttribute("codeList", codeList);
        }

        if(ObjectUtils.isNotEmpty(attendDate)){
            inputDate = LocalDate.parse(attendDate, df);
        }else{
            inputDate = LocalDate.now().minusDays(1);
        }

        Integer dayOfWeek = inputDate.getDayOfWeek().getValue();

        if(attendType.equals("") && dayOfWeek.equals(3)){ // 수요일은 기본 수요예배
            attendType = "wednesday";
        }

        log.info("attendType ====> {}", attendType);

        String worshipType = dayOfWeek.equals(3) ? attendType : attendService.getWorshipType(dayOfWeek);

        String returnVal = "";

        // 부서장은 해당부서의 출석현황만 볼 수 있게.
        if(sessionUser.getPart().equals("staff")){

            sessionUser.getRoleList().forEach(role -> {
                // department, gender

            });

            returnVal = "/attend/view";
        }else{
            // 전체부서에 대한 출석현황

            log.info("worshipType ====> {}", worshipType);
            log.info("addendDate ====> {}", inputDate);

            departmentCountList.forEach(depart -> {

                Integer attendCount = attendService.countByDepartmentAndAttendType(depart.getDepartmentSeqNo(), worshipType, inputDate);

                log.info("attendCount ====> {}", attendCount);

                depart.setAttendCount(attendCount);

                Float attendRate = attendCount.floatValue() / depart.getCount() * 100;

                depart.setAttendRate(attendRate);

                log.info("depart =====> {}", depart.toString());

            });

            returnVal = "/attend/list";
        }

        session.setAttribute("attend", url.toString());

        model.addAttribute("navi", "attend");
        //model.addAttribute("page", attendPage);
        model.addAttribute("code", code);
        //model.addAttribute("name", name);
        model.addAttribute("attendList", departmentCountList);
        model.addAttribute("attendDate", ObjectUtils.isEmpty(attendDate) ? inputDate.format(df) : attendDate);
        model.addAttribute("attendType", worshipType);
        model.addAttribute("dayOfWeek", dayOfWeek);

        return returnVal;
    }

}
