package com.mms.controller;

import com.mms.model.*;
import com.mms.repository.AccountRoleRepository;
import com.mms.repository.CodeRepository;
import com.mms.repository.DepartmentRepository;
import com.mms.service.UserService;
import com.mms.util.FunctionUtil;
import com.mms.util.PageWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Controller
@RequestMapping("/user")
public class UserController extends FunctionUtil {

    private Map<String, Object> map;
    private final String NAVI = "user";
    private static final String repository = "/repository";

    private SessionUser sessionUser;

    private List<Department> departmentList;
    private List<AccountRole> accountRoleList;

    private List<Code> codeList;

    private PageWrapper<User> page;
    private PageWrapper<Counseling> counselingPage;
    private User user;

    private StringBuffer url;

    private Calendar birth;
    private Calendar today;

    private List<Relation> falimyList;
    private Long temp;

    private List<String> relationNameList = new ArrayList<String>(){
        {

            add("남편");
            add("아내");
            add("아들");
            add("딸");
            add("할아버지");
            add("할머니");
            add("외할아버지");
            add("외할머니");
            add("아버지");
            add("어머니");
            add("큰아버지");
            add("큰어머니");
            add("작은아버지");
            add("작은어머니");
            add("고모");
            add("고모부");
            add("이모");
            add("이모부");
            add("외삼촌");
            add("외숙모");
            add("형");
            add("누나");
            add("언니");
            add("오빠");
            add("남동생");
            add("여동생");
            add("사촌형");
            add("사촌누나");
            add("사촌언니");
            add("사촌오빠");
            add("사촌남동생");
            add("사촌여동생");
            add("외사촌형");
            add("외사촌누나");
            add("외사촌언니");
            add("외사촌오빠");
            add("외사촌남동생");
            add("외사촌여동생");
            add("손자");
            add("손녀");
            add("외손자");
            add("외손녀");
        }
    };

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private AccountRoleRepository accountRoleRepository;


    @GetMapping("/form")
    public String user(
            Model model,
            User user,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        // 부서장일때는 해당 부서만 나머지는 모든 부서목록가져오기
        if(sessionUser.getPart().equals("staff")){
            accountRoleList = new ArrayList<>();
            accountRoleList = accountRoleRepository.findByAccountIdOrderByDepartmentIdAsc(sessionUser.getId());

            List<AccountRole> distinctList = new ArrayList<>();

            temp = 0l;

            accountRoleList.forEach(r -> {

                if(!temp.equals(r.getDepartmentId())){
                    distinctList.add(r);
                }
                temp = r.getDepartmentId();
            });


            model.addAttribute("departmentList", distinctList);
        }else{
            departmentList = new ArrayList<>();
            departmentList = departmentRepository.findAllByOrderByArrayAsc();
            model.addAttribute("departmentList", departmentList);
        }





        if(sessionUser.getPart().equals("supreme")){
            codeList = new ArrayList<>();
            codeList = codeRepository.findAllByOrderByNameAsc();

            model.addAttribute("codeList", codeList);
        }

        user.setThumbnail("/img/default-avatar.png");

        model.addAttribute("navi", NAVI);
        model.addAttribute("user", user);
        model.addAttribute("relationNameList", relationNameList);
        model.addAttribute("jobList", jobItem);
        model.addAttribute("specialUnitList", specialUnitItem);
        model.addAttribute("starList", starItem);

        return "/user/form";
    }

    public static Predicate distinctByKey(Function keyExtractor) {
        Map seen = new ConcurrentHashMap();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @GetMapping("/modify/{secure}")
    public String modify(
            @PathVariable String secure,
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        // 부서장일때는 해당 부서만 나머지는 모든 부서목록가져오기
        if(sessionUser.getPart().equals("staff")){
            accountRoleList = new ArrayList<>();
            accountRoleList = accountRoleRepository.findByAccountIdOrderByDepartmentIdAsc(sessionUser.getId());

            List<AccountRole> distinctList = new ArrayList<>();

            temp = 0l;

            accountRoleList.forEach(r -> {

                if(!temp.equals(r.getDepartmentId())){
                    distinctList.add(r);
                }
                temp = r.getDepartmentId();
            });


            model.addAttribute("departmentList", distinctList);
        }else{
            departmentList = new ArrayList<>();
            departmentList = departmentRepository.findAllByOrderByArrayAsc();
            model.addAttribute("departmentList", departmentList);
        }

        if(sessionUser.getPart().equals("supreme")){
            codeList = new ArrayList<>();
            codeList = codeRepository.findAllByOrderByNameAsc();

            model.addAttribute("codeList", codeList);
        }

        user = new User();
        user = userService.getUser(secure);

        model.addAttribute("navi", NAVI);
        model.addAttribute("user", user);
        model.addAttribute("relationNameList", relationNameList);
        model.addAttribute("jobList", jobItem);
        model.addAttribute("specialUnitList", specialUnitItem);
        model.addAttribute("starList", starItem);

        return "/user/form";
    }

    @PostMapping("/regist")
    public String regist(
            UserDTO.general dto,
            HttpSession session,
            HttpServletRequest req
    ) throws Exception {

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        String path = "";

        if(req.getRequestURL().toString().contains("localhost:8080")){
            //path = "/Users/tommykim/Documents/MorningStarManagement/src/main/resources/static/repository";
            path = "/Users/kimtaeyeon/Documents/MorningStarManagement/src/main/resources/static/repository";
        }else{
            path = "/tom0718/tomcat/webapps/ROOT/WEB-INF/classes/static/repository";
        }


        userService.regist(dto, sessionUser, path);

        return "redirect:/user/list";
    }

    @RequestMapping("/list")
    public String list(
            @PageableDefault(sort = { "seqNo" }, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @RequestParam(value = "code", defaultValue = "") String code,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "department", defaultValue = "0") Long department,
            @RequestParam(value = "gender", defaultValue = "M,F") String gender,
            @RequestParam(value = "star", defaultValue = "") String star,
            @RequestParam(value = "specialUnit", defaultValue = "") String specialUnit,
            @RequestParam(value = "job", defaultValue = "") String job,
            @RequestParam(value = "age", required = false) Integer age,
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        url = new StringBuffer();

        url.append("/user/list?1=1");

        if(sessionUser.getPart().equals("supreme")){
            if(!code.equals("")){
                url.append("&code=");
                url.append(code);
            }
        }

        if(sessionUser.getPart().equals("supreme") || sessionUser.getPart().equals("pastor")){
            if(!department.equals(0l)){
                url.append("&department=");
                url.append(department);
            }
            if(!gender.equals("")){
                url.append("&gender=");
                url.append(gender);
            }
            if(!star.equals("")){
                url.append("&star=");
                url.append(star);
            }
        }

        if(!name.equals("")){
            url.append("&name=");
            url.append(name);
        }
        if(!specialUnit.equals("")){
            url.append("&specialUnit=");
            url.append(specialUnit);
        }
        if(!job.equals("")){
            url.append("&job=");
            url.append(job);
        }
        if(ObjectUtils.isNotEmpty(age)){
            url.append("&age=");
            url.append(age);
        }


        page = new PageWrapper<User>(userService.getUserList(pageable, sessionUser, code, name, department, gender, star, specialUnit, job, age), url.toString());

        url.append("&page=");
        url.append(pageable.getPageNumber());

        session.setAttribute("user", url.toString());

        today = new GregorianCalendar();

        page.getContent().forEach(u -> {
            birth = new GregorianCalendar();
            birth.setTime(u.getBirthday());
            u.setAge(today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + 1);
        });

        if(sessionUser.getPart().equals("supreme")){
            codeList = new ArrayList<>();
            codeList = codeRepository.findAllByOrderByNameAsc();

            model.addAttribute("codeList", codeList);
        }

        if(sessionUser.getPart().equals("supreme") || sessionUser.getPart().equals("pastor")){
            departmentList = new ArrayList<>();
            departmentList = departmentRepository.findAllByOrderByArrayAsc();

            model.addAttribute("departmentList", departmentList);
        }

        model.addAttribute("navi", NAVI);
        model.addAttribute("page", page);
        model.addAttribute("code", code);
        model.addAttribute("name", name);
        model.addAttribute("department", department);
        model.addAttribute("gender", gender);
        model.addAttribute("star", star);
        model.addAttribute("starList", starItem);
        model.addAttribute("specialUnit", specialUnit);
        model.addAttribute("specialUnitList", specialUnitItem);
        model.addAttribute("job", job);
        model.addAttribute("age", age);
        model.addAttribute("jobList", jobItem);


        return "/user/list";
    }

    @GetMapping("/view/{secure}")
    public String view(
            @PathVariable String secure,
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        departmentList = new ArrayList<>();
        departmentList = departmentRepository.findAllByOrderByArrayAsc();

        if(sessionUser.getPart().equals("supreme")){
            codeList = new ArrayList<>();
            codeList = codeRepository.findAllByOrderByNameAsc();

            model.addAttribute("codeList", codeList);
        }

        user = new User();
        user = userService.getUser(secure);

        today = new GregorianCalendar();
        birth = new GregorianCalendar();
        birth.setTime(user.getBirthday());
        user.setAge(today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + 1);

        // TODO 연결된 가족 가져오기
        falimyList = new ArrayList<>();
        falimyList = userService.getRelationList(user.getSeqNo());

        falimyList.forEach(f -> {
            birth = new GregorianCalendar();
            birth.setTime(f.getRelation().getBirthday());
            f.getRelation().setAge(today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + 1);
        });

        model.addAttribute("navi", NAVI);
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("user", user);
        model.addAttribute("relationNameList", relationNameList);
        model.addAttribute("familyList", falimyList);


        return "/user/view";
    }

    @GetMapping("/counseling/{userSeqNo}/list")
    public String counselingList(
            @PageableDefault(sort = { "seqNo" }, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @PathVariable(value = "userSeqNo") Long userSeqNo,
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        url = new StringBuffer();

        url.append("/user/counseling/");
        url.append(userSeqNo);
        url.append("/list");

        counselingPage = new PageWrapper<Counseling>(userService.getCounselingList(pageable, sessionUser, userSeqNo), url.toString());

        model.addAttribute("page", counselingPage);

        return "/user/counseling_list";
    }

    @GetMapping("/connection/list")
    public String connectionList(
            @PageableDefault(sort = { "seqNo" }, direction = Sort.Direction.ASC, size = 10) Pageable pageable,
            @RequestParam(value = "me", required = true) Long me,
            @RequestParam(value = "department", defaultValue = "0") Long department,
            @RequestParam(value = "user", defaultValue = "") String user,
            @RequestParam(value = "gender", defaultValue = "M,F") String gender,
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        url = new StringBuffer();

        url.append("/user/connection/list");
        url.append("?me=");
        url.append(me);
        url.append("&department=");
        url.append(department);
        url.append("&gender=");
        url.append(gender);
        url.append("&user=");
        url.append(user);

        page = new PageWrapper<User>(userService.connectionList(pageable, sessionUser, me, department, gender, user), url.toString());

        page.getContent().forEach(u -> {
            birth = new GregorianCalendar();
            birth.setTime(u.getBirthday());
            u.setAge(today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + 1);
        });

        model.addAttribute("page", page);

        return "/user/connection_list";
    }

    @PostMapping("/saveCounseling")
    @ResponseBody
    public Map<String, Object> saveCounseling(
            Long userSeqNo,
            @RequestParam(value = "seqNo", required = false) Long seqNo,
            String title,
            String content,
            String handling,
            HttpSession session
    ){

        map = new HashMap<>();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        map = userService.saveCounseling(userSeqNo, seqNo, title, content, handling, sessionUser.getId());

        return map;
    }

    @PostMapping("/getCounseling")
    @ResponseBody
    public Map<String, Object> getCounseling(
            @RequestParam(value = "seqNo", required = true) Long seqNo,
            HttpSession session
    ){

        map = new HashMap<>();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        map = userService.getCounseling(seqNo);

        return map;
    }

    @PostMapping("/deleteCounseling")
    @ResponseBody
    public Map<String, Object> deleteCounseling(
            @RequestParam(value = "seqNo", required = true) Long seqNo,
            HttpSession session
    ){

        map = new HashMap<>();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        map = userService.deleteCounseling(seqNo);

        return map;
    }

    @PostMapping("/saveRelationship")
    @ResponseBody
    public Map<String, Object> saveRelationship(
            @RequestParam(value = "me", required = true) Long me,
            @RequestParam(value = "userSeqNo", required = true) Long userSeqNo,
            @RequestParam(value = "relationName", required = true) String relationName,
            HttpSession session
    ){

        map = new HashMap<>();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        map = userService.saveRelationship(me, userSeqNo, relationName);

        return map;
    }

    @PostMapping("/removeRelationship")
    @ResponseBody
    public Map<String, Object> removeRelationship(
            @RequestParam(value = "me", required = true) Long me,
            @RequestParam(value = "family", required = true) Long family,
            HttpSession session
    ){

        map = new HashMap<>();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        map = userService.removeRelationship(me, family);

        return map;
    }

    @PostMapping("/deleteUser")
    @ResponseBody
    public Map<String, Object> deleteUser(
            @RequestParam(value = "seqNo", required = true) Long seqNo,
            HttpSession session
    ){

        map = new HashMap<>();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        map = userService.deleteUser(seqNo, sessionUser.getId());

        return map;
    }

    @GetMapping("/downloadFile")
    public ModelAndView downLoadFile(HttpServletRequest req, Long id) {

        String path = "";

        if(req.getRequestURL().toString().contains("localhost:8080")){
            //path = "/Users/tommykim/Documents/MorningStarManagement/src/main/resources/static/repository";
            path = "/Users/kimtaeyeon/Documents/MorningStarManagement/src/main/resources/static/repository";
        }else{
            path = "/tom0718/tomcat/webapps/ROOT/WEB-INF/classes/static/repository";
        }

        user = new User();
        user = userService.getUserBySeqNo(id);

        String thumbnail = user.getThumbnail().replace("/repository/","");


        ModelAndView mav = new ModelAndView();
        mav.addObject("path", path);
        mav.addObject("file", thumbnail);
        mav.addObject("fileName", ObjectUtils.isEmpty(user.getReName()) ? user.getRealName() : user.getReName());
        mav.setViewName("downloadFile");

        return mav;
    }

    @GetMapping("/convertPhone")
    @ResponseBody
    public String convertPhone(
            HttpSession session
    ){

        userService.convertPhoneNumber();

        return "success";
    }
}
