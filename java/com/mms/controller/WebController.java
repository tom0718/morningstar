package com.mms.controller;

import com.mms.model.*;
import com.mms.repository.AccountRepository;
import com.mms.repository.CodeRepository;
import com.mms.repository.DepartmentRepository;
import com.mms.service.AccountService;
import com.mms.service.UserAttendService;
import com.mms.service.UserService;
import com.mms.util.FunctionUtil;
import com.mms.util.PageWrapper;
import com.mms.util.SecureUtil;
import com.mms.util.TodayColor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@Slf4j
public class WebController extends FunctionUtil{

	private Map<String, Object> map;

	private Account account;
	
	private Integer writing;
	private Integer waiting;
	private Integer hold;
	private Integer returns;
	private Integer approval;
	private Integer transfer;
	private Integer i;

	private SessionUser sessionUser;

	private StringBuffer url;

	private PageWrapper<SearchUser> page;
	
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Calendar birth;
	private Calendar today;

	private List<Department> departmentList;

	private List<Code> codeList;
	private SearchUser user;
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private CodeRepository codeRepository;

	@Autowired
	private UserAttendService userAttendService;

	
	@GetMapping("/")
	public String index() {
		
		return "redirect:/login";
	}
	

	
	@GetMapping("/login")
	public String login(Model model){
		
		return "/user/login";
	}
	
	@GetMapping("/errors")
	public String error(Model model){
		
		return "/error/error";
	}
	
	@GetMapping("/startMailSystem/{id}")
	@ResponseBody
	public String startMailSystem(
			@PathVariable("id") String id,
			HttpSession session,
			HttpServletRequest req,
			Model model) {
		
		//log.info("run mailSystem ==> " + id);
		
		return id;
	}


	@PostMapping("/findItem")
	@ResponseBody
	public Map<String, Object> findItem(
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "item", required = true) String item,
			HttpSession session){

		map = new HashMap<>();

		map = accountService.findItem(type, item);

		return map;
	}


	@RequestMapping("/userList")
	public String list(
			@PageableDefault(sort = { "seqNo" }, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
			@RequestParam(value = "code", defaultValue = "") String code,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "department", defaultValue = "0") Long department,
			@RequestParam(value = "gender", defaultValue = "M,F") String gender,
			Model model,
			HttpSession session
	){

		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);

		url = new StringBuffer();

		url.append("/userList?1=1");

		if(sessionUser.getPart().equals("supreme")){
			if(!code.equals("")){
				url.append("&code=");
				url.append(code);
			}
		}

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

		page = new PageWrapper<SearchUser>(userService.getSearchUserList(pageable, sessionUser, code, name, department, gender), url.toString());

		url.append("&page=");
		url.append(pageable.getPageNumber());

		session.setAttribute("userList", url.toString());

		today = new GregorianCalendar();

		Date now = new Date();

		page.getContent().forEach(u -> {
			birth = new GregorianCalendar();
			birth.setTime(u.getBirthday());
			u.setAge(today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + 1);
			u.setStrPhone(SecureUtil.maskingMobileNumber(u.getPhone()));
		});

		if(sessionUser.getPart().equals("supreme")){
			codeList = new ArrayList<>();
			codeList = codeRepository.findAllByOrderByNameAsc();

			model.addAttribute("codeList", codeList);
		}

		departmentList = new ArrayList<>();
		departmentList = departmentRepository.findAllByOrderByArrayAsc();

		String color = TodayColor.getTodayColor();

		model.addAttribute("departmentList", departmentList);

		model.addAttribute("navi", "userList");
		model.addAttribute("page", page);
		model.addAttribute("code", code);
		model.addAttribute("name", name);
		model.addAttribute("department", department);
		model.addAttribute("gender", gender);
		model.addAttribute("color", color);

		return "/user/userList";
	}

	@GetMapping("/userView/{secure}")
	public String userView(
			@PathVariable String secure,
			Model model,
			HttpSession session
	){

		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);

		if(sessionUser.getPart().equals("supreme")){
			codeList = new ArrayList<>();
			codeList = codeRepository.findAllByOrderByNameAsc();

			model.addAttribute("codeList", codeList);
		}

		user = new SearchUser();
		user = userService.getSearchUser(secure);

		today = new GregorianCalendar();
		birth = new GregorianCalendar();
		birth.setTime(user.getBirthday());
		user.setAge(today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + 1);
		user.setStrPhone(SecureUtil.maskingMobileNumber(user.getPhone()));

		boolean checkAttend = false;
		// TODO 출석체크(수요일, 일요일만)
		LocalDate localDate = LocalDate.now();
		int dayOfWeekNumber = localDate.getDayOfWeek().getValue();
		// 3:수요일, 7:일요일
		if(dayOfWeekNumber == 3 || dayOfWeekNumber == 7){

			if(dayOfWeekNumber == 3){
				checkAttend = userAttendService.checkAttendWorship(user.getSeqNo(), localDate, "wednesday");
			}else if(dayOfWeekNumber == 7){
				checkAttend = userAttendService.checkAttendWorship(user.getSeqNo(), localDate, "sunday");
			}
		}

		model.addAttribute("dayOfWeekNumber", dayOfWeekNumber);
		model.addAttribute("checkAttend", checkAttend);

		model.addAttribute("navi", "userList");
		model.addAttribute("user", user);


		return "/user/searchView";
	}

	@GetMapping("/check")
	public String userCheckForm(
			Model model,
			HttpSession session
	){



		return "/user/check_login";
	}

	@PostMapping("/checkMember")
	@ResponseBody
	public Map<String, Object> userCheckMember(
			String name,
			String phone,
			Model model,
			HttpSession session
	){
		map = new HashMap<>();

		Optional<SearchUser> searchUser = userService.checkMember(name, phone, "", "checkMember");

		map.put("result", searchUser.isPresent() ? "success" : "fail");

		log.info("optional =======> {}", searchUser.isPresent());

		boolean checkAttend = false;
		boolean checkDawnAttend = false;
		boolean checkTime = false;

		if(searchUser.isPresent()){

			// TODO 출석체크(수요일, 일요일만)
			LocalDate localDate = LocalDate.now();
			int dayOfWeekNumber = localDate.getDayOfWeek().getValue();
			// 3:수요일, 7:일요일
			LocalTime localTime = LocalTime.now();
			int time = localTime.getHour();

			if(dayOfWeekNumber == 3 || dayOfWeekNumber == 7){

				// 예배 2시간전부터 출석체크, 수요일:18시, 일요일:8시
				if(dayOfWeekNumber == 3 && time >= 18){
					checkAttend = userAttendService.checkAttendWorship(searchUser.get().getSeqNo(), localDate, "wednesday");
					checkTime = true;
				}else if(dayOfWeekNumber == 7 && time >= 8){
					checkAttend = userAttendService.checkAttendWorship(searchUser.get().getSeqNo(), localDate, "sunday");
					checkTime = true;
				}
			}

			if(dayOfWeekNumber != 7){
				checkDawnAttend = userAttendService.checkAttendWorship(searchUser.get().getSeqNo(), localDate, "dawn");
			}

			map.put("dayOfWeekNumber", dayOfWeekNumber);
			map.put("secure", searchUser.get().getSecure());
			map.put("checkAttend", checkAttend);
			map.put("checkDawnAttend", checkDawnAttend);
			map.put("checkTime", checkTime);
			map.put("time", time);
		}

		return map;
	}

	@PostMapping("/checkView")
	public String checkView(
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "secure", required = true) String secure,
			@RequestParam(value = "attendType", defaultValue = "") String attendType,
			@RequestParam(value = "worshipType", defaultValue = "") String worshipType,
			@RequestParam(value = "reason", defaultValue = "") String reason,
			Model model,
			HttpSession session
	){

		Optional<SearchUser> searchUser = userService.checkMember(name, phone, secure, "checkView");

		log.info("view data =======> {}", name+","+phone+","+secure);

		log.info("view optional =======> {}", searchUser.isPresent());
		if(searchUser.isPresent()){

			log.info("user ========> {}", searchUser.get().toString());

			LocalDate localDate = LocalDate.now();

			// TODO 출석체크
			if(ObjectUtils.isNotEmpty(attendType) && ObjectUtils.isNotEmpty(worshipType)){
				userAttendService.saveAttend(searchUser.get().getSeqNo(), searchUser.get().getDepartmentSeqNo(), searchUser.get().getGender(), worshipType, localDate, attendType, reason);
			}

			String color = TodayColor.getTodayColor();

			model.addAttribute("user", searchUser.get());
			model.addAttribute("color", color);

			return "/user/check_view";
		}else{

			return "redirect:/check";
		}


	}

	@PostMapping("/changeProfileImage")
	@ResponseBody
	public Map<String, Object> changeProfileImage(
			MultipartFile image,
			String secure,
			HttpServletRequest req
	){
		map = new HashMap<>();

		String path = "";

		if(req.getRequestURL().toString().contains("localhost:8080")){
			//path = "/Users/tommykim/Documents/MorningStarManagement/src/main/resources/static/repository";
			path = "/Users/kimtaeyeon/Documents/MorningStarManagement/src/main/resources/static/repository";
		}else{
			path = "/tom0718/tomcat/webapps/ROOT/WEB-INF/classes/static/repository";
		}

		map = userService.changeProfileImage(image, secure, path);

		return map;
	}

	@PostMapping("/enrollAttend")
	@ResponseBody
	public Map<String, Object> enrollAttend(
			String secure,
			HttpSession session
	){
		map = new HashMap<>();

		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);

		map = userAttendService.enrollAttend(secure, sessionUser.getId());

		return map;
	}


}
