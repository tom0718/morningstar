package com.mms.controller;

import com.mms.model.*;
import com.mms.repository.AccountRepository;
import com.mms.repository.CodeRepository;
import com.mms.repository.DepartmentRepository;
import com.mms.service.AccountService;
import com.mms.util.FunctionUtil;
import com.mms.util.PageWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/account")
public class AccountController extends FunctionUtil{

	private Map<String, Object> map;

	private AccountDTO accountDTO;
	private Account account;
	private Account resAccount;
	private PageWrapper<Account> page;
	private final String NAVI = "account";
	private final String HEAD = "account";

	private List<Account> accountList;
	private List<Department> departmentList;

	private SessionUser sessionUser;
	private StringBuilder url;

	private List<Code> codeList;

	@Autowired
	private AccountService accountService;

	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private CodeRepository codeRepository;

	@GetMapping("/loginProcess")
	public String loginProcess(
			Model model,
			HttpServletRequest req,
			HttpSession session,
			HttpServletResponse res) throws Exception {


		String id = SecurityContextHolder.getContext().getAuthentication().getName();

		account = new Account();
		account = accountRepository.getOne(id);

		// check status 1:사용중., 2:비번잠김, 3:사용안함,
		if(account.getStatus() == 1) {

			sessionUser = new SessionUser();
			sessionUser.setCode(account.getCode());
			sessionUser.setId(account.getId());
			sessionUser.setAuth(account.getAuth());
			sessionUser.setPart(account.getPart());
			sessionUser.setName(account.getName());
			sessionUser.setRoleList(account.getAccountRoleList());

			session.setAttribute(SessionName, sessionUser);

			String returnVal = "";

			if(account.getPart().equals("word")) {
				returnVal = "redirect:/word/list";
			}else{
				returnVal = "redirect:/dashboard";
			}

			return returnVal;

		}else{
			return "redirect:/login?error=";
		}

	}


	
	
	@RequestMapping("/list")
	public String companyList(
			@PageableDefault(sort = { "joined" }, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
			@RequestParam(value="code", defaultValue="") String code,
			@RequestParam(value="id", defaultValue="") String id,
			@RequestParam(value="name", defaultValue="") String name,
			@RequestParam(value="division", defaultValue="0") Long division,
			@RequestParam(value="status", defaultValue="1,2,3") String status,
			@RequestParam(value="part", defaultValue="") String part,
			HttpSession session,
			HttpServletRequest req,
			Model model) {
		
		url = new StringBuilder();

		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);

		if(ObjectUtils.isEmpty(sessionUser)){
			return "redirect:/login";
		}

		url.append("/account/list?1=1");

		if(!code.equals("")) {
			url.append("&code=");
			url.append(code);
		}
		if(!name.equals("")) {
			url.append("&name=");
			url.append(name);
		}
		if(!id.equals("")) {
			url.append("&id=");
			url.append(id);
		}
		if(!division.equals("")) {
			url.append("&division=");
			url.append(division);
		}
		if(!status.equals("")) {
			url.append("&status=");
			url.append(status);
		}

		if(!part.equals("")) {
			url.append("&part=");
			url.append(part);
		}


		page = new PageWrapper<>(accountService.getList(pageable, code, name, division, status, part, id, sessionUser), url.toString());

		departmentList = new ArrayList<>();
		departmentList = departmentRepository.findAllByOrderByArrayAsc();

		codeList = new ArrayList<>();
		codeList = codeRepository.findAllByOrderByNameAsc();

		model.addAttribute("departmentList", departmentList);
		model.addAttribute("codeList", codeList);

		model.addAttribute("navi", NAVI);
		model.addAttribute("head", HEAD);
		model.addAttribute("page", page);
		model.addAttribute("code", code);
		model.addAttribute("name", name);
		model.addAttribute("division", division);
		model.addAttribute("status", status);
		model.addAttribute("id", id);
		model.addAttribute("part", part);
		model.addAttribute("account", account);

		return "/account/list";
	}
	
	
	@GetMapping("/enroll")
	public String accountForm(
			HttpSession session,
			HttpServletRequest req,
			Account account,
			Model model) {


		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);

		if(ObjectUtils.isEmpty(sessionUser)){
			return "redirect:/login";
		}

		account.setPart(sessionUser.getPart().equals("supreme") ? "admin" : "staff");

		if(sessionUser.getPart().equals("supreme")){
			codeList = new ArrayList<>();
			codeList = codeRepository.findAllByOrderByNameAsc();

			model.addAttribute("codeList", codeList);
		}


		departmentList = new ArrayList<>();
		departmentList = departmentRepository.findAllByOrderByArrayAsc();

		model.addAttribute("navi", "account");
		model.addAttribute("departmentList", departmentList);
		model.addAttribute("account", account);
		model.addAttribute("password", "");

		model.addAttribute("action","/account/regist");

		return "/account/account";
	}
	
	@PostMapping("/regist")
	public String insertAccount(
			@ModelAttribute AccountDTO.General accountDTO,
			HttpSession session,
			HttpServletRequest req
			) {

		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);


		if(ObjectUtils.isEmpty(sessionUser)){
			return "redirect:/login";
		}

		accountService.insertAccount(accountDTO, sessionUser,"insert");

		return "redirect:/account/list";
	}
	
	@GetMapping("/modify/{auth}")
	public String accountModify(
			@PathVariable("auth") String auth,
			HttpSession session,
			HttpServletRequest req,
			Model model) {

		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);

		if(ObjectUtils.isEmpty(sessionUser)){
			return "redirect:/login";
		}

		account = new Account();
		account = accountService.getAccount(auth, sessionUser.getCode());

		departmentList = new ArrayList<>();
		departmentList = departmentRepository.findAllByOrderByArrayAsc();

		if (sessionUser.getPart().equals("supreme")) {
			codeList = new ArrayList<>();
			codeList = codeRepository.findAllByOrderByNameAsc();

			model.addAttribute("codeList", codeList);
		}

		model.addAttribute("departmentList", departmentList);

		model.addAttribute("account", account);
		model.addAttribute("navi", NAVI);
		model.addAttribute("password", "");
		model.addAttribute("action", "/account/modify");

		return "/account/account";
	}
	

	@PostMapping("/modify")
	public String accountModifyAction(
			@ModelAttribute AccountDTO.General accountDTO,
			HttpSession session,
			HttpServletRequest req
	) {

		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);

		if(ObjectUtils.isEmpty(sessionUser)){
			return "redirect:/login";
		}

		accountService.insertAccount(accountDTO, sessionUser, "modify");

		return "redirect:/account/list";
	}


	@PostMapping("/checkId")
	@ResponseBody
	public Map<String, Object> checkId(
			String id
	) throws Exception {

		map = new HashMap<>();

		map = accountService.checkId(id);

		return map;
	}

	@PostMapping("/checkEmail")
	@ResponseBody
	public Map<String, Object> checkEmail(
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "id", required = false) String id
	) throws Exception {

		map = new HashMap<>();

		map = accountService.checkEmail(id,email);

		return map;
	}


	
	@PostMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(
			String id,
			HttpSession session,
			HttpServletRequest req
			) {
		map = new HashMap<>();
		
		Integer result = accountService.deleteAccount(id);

		map.put("result", result);
		
		return map;
	}

	@GetMapping("/profile")
	public String modifyProfile(
			HttpSession session,
			Model model
	) {

		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);

		if(ObjectUtils.isEmpty(sessionUser)){
			return "redirect:/login";
		}

		Optional<Account> opAccount = accountService.getProfile(sessionUser.getId());

		model.addAttribute("account", opAccount.get());

		return "/account/profile";
	}


	@PostMapping("/modifyProfile")
	@ResponseBody
	public Map<String, Object> modifyProfile(
			@ModelAttribute AccountDTO.General accountDTO,
			HttpSession session,
			HttpServletRequest req
	) {

		map = new HashMap<>();

		sessionUser = new SessionUser();
		sessionUser = (SessionUser) session.getAttribute(SessionName);

		accountDTO.setAuth(sessionUser.getAuth());

		map = accountService.modifyProfile(accountDTO);

		return map;
	}




	
}
