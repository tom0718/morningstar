package com.mms.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;

import com.mms.model.AccountRole;
import com.mms.model.SessionUser;
import com.mms.repository.AccountRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mms.model.Account;
import com.mms.model.AccountDTO;
import com.mms.repository.AccountRepository;
import com.mms.util.FunctionUtil;

@Service
@Slf4j
public class AccountService extends FunctionUtil{

	private Map<String,Object> map;

	private Account account;
	
	private StringBuilder sb;
	
	private static Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$");
	
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date start;
	private Date end;

	private List<Integer> statusList;
	private List<String> partList;

	private AccountRole accountRole;

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private PasswordEncoder password;
	
	@Autowired
	private AccountRepository accountRepository;
	
	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private AccountRoleRepository accountRoleRepository;
	
	@Transactional
	public void insertAccount(AccountDTO.General inputAccount, SessionUser sessionUser, String type) {
		
		account = new Account();

		String pwd = "";
		
		if(type.equals("insert")) {

			account.setId(inputAccount.getId());
			account.setAuth(KeyGenerators.string().generateKey());


			if(inputAccount.getPart().equals("admin")){
				account.setCode(inputAccount.getCode());
			}else{
				account.setCode(sessionUser.getCode());
			}

			account.setDeleted(false);
			account.setJoined(Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()));
			account.setFail(0);
			account.setStatus(1);


		}else {
			account = accountRepository.getOne(inputAccount.getId());

			account.setStatus(inputAccount.getStatus());
			account.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()));

			pwd = account.getPassword();
		}

		account.setPart(inputAccount.getPart());
		account.setName(inputAccount.getName());

		if(ObjectUtils.isNotEmpty(inputAccount.getPassword())){
			account.setPassword(password.encode(inputAccount.getPassword()));
		}else{
			account.setPassword(pwd);
		}

		account.setPhone(inputAccount.getPhone());




		account.setEmail(inputAccount.getEmail());
		account.setNote(inputAccount.getNote());

		account = accountRepository.save(account);

		accountRoleRepository.deleteByAccountId(account.getId());

		if(inputAccount.getPart().equals("staff")){

			inputAccount.getRoleList().forEach(r -> {
				accountRole = new AccountRole();
				accountRole.setAccountId(account.getId());
				accountRole.setDepartmentId(r.getDepartment());
				accountRole.setMale(r.getGender().equals("M") ? true : false);
				accountRole.setFemale(r.getGender().equals("F") ? true : false);

				accountRoleRepository.save(accountRole);
			});
		}
		
	}
	


	public Page<Account> getList(Pageable pageable, String code, String name, Long division, String status, String part, String id, SessionUser sessionUser) {

		@SuppressWarnings("serial")
		Specification<Account> specification = new Specification<Account>() {

			@Override
			public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = new ArrayList<>();

				if(!sessionUser.getPart().equals("supreme")){
					predicates.add(builder.equal(root.get("code"), sessionUser.getCode())); // SessionSessionUser..getCode()
				}

				if(sessionUser.getPart().equals("supreme")){
					//predicates.add(builder.equal(root.get("part"), "admin")); // 관리자만
				}else if(sessionUser.getPart().equals("admin")){
					//String[] partArray = {"staff","pastor","word"};
					predicates.add(builder.notEqual(root.get("part"), "admin")); // 부서장, 목회자, 말씀
				}

				predicates.add(builder.equal(root.get("deleted"), false));
				predicates.add(builder.notEqual(root.get("part"), "supreme"));
				predicates.add(builder.notEqual(root.get("id"), sessionUser.getId()));
				
				if (!name.equals("")) {
					predicates.add(builder.like(root.get("name"), "%" + name + "%"));
				}

				if (!id.equals("")) {
					predicates.add(builder.like(root.get("id"), "%" + id + "%"));
				}

				if (!code.equals("")) {
					predicates.add(builder.equal(root.get("code"), code));
				}

				// join accountRole
				if (!division.equals(0l)){

					Join<Account, AccountRole> join = root.join("accountRoleList");

					predicates.add(builder.equal(join.get("departmentId"), division));

				}
				
				if (!status.equals("")) {

					String[] arr = status.split(",");

					statusList = new ArrayList<>();

					for(String st: arr){
						statusList.add(new Integer(st));
					}

					predicates.add(builder.in(root.get("status")).value(statusList));

				}

				if (!part.equals("")) {

					predicates.add(builder.equal(root.get("part"), part));

				}
				
				query.where(predicates.toArray(new Predicate[] {}));

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};

		return accountRepository.findAll(specification, pageable);
	}
	





	@Transactional
	public Integer deleteAccount(String id) {
		return accountRepository.updateDeleteFlag(id);
	}



	@SuppressWarnings("unchecked")
	public List<Account> searchPopupSessionUser(String word, String company) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("select c from accounts c where c.enable = 3 and c.delete = false");
		
		if(word != null && !word.equals("")) {
			sb.append(" and (c.id like concat('%',:word,'%') or c.name like concat('%',:word,'%'))");
		}
		if(company != null && !company.equals("")) {
			sb.append(" and c.company = :company");
		}
		
		sb.append(" order by name asc");
		
		Query query = entityManager.createQuery(sb.toString());
		
		if(word != null && !word.equals("")) {
			query.setParameter("word", word);
		}

		if(company != null && !company.equals("")) {
			query.setParameter("company", company);
		}
		
		return query.getResultList();
		
			
	}


    public Map<String, Object> checkId(String id) {
		map = new HashMap<>();

		Boolean exist = accountRepository.existsById(id);

		map.put("exist", exist);

		return map;
    }

	public Account getAccount(String auth, String code) {
		return accountRepository.findByDeletedAndAuthAndCode(false, auth, code);
	}

    public Map<String, Object> findItem(String type, String item) {
		map = new HashMap<>();

		String subject = "";
		sb = new StringBuilder();
		String pwd = "";

		account = new Account();

		// TODO status 3 : 사용중지된 계정은 관리자에게 문의바람 메세지 출력.

		if(type.equals("id")){
			subject = "[MMS]아이디 찾기";
			account = accountRepository.findByEmailAndDeleted(item, false);

			if(ObjectUtils.isNotEmpty(account) && !account.getStatus().equals(3)){
				sb.append("등록된 아이디는 ");
				sb.append(maskId(account.getId()));
				sb.append(" 입니다.");
			}

		}else if(type.equals("pwd")){
			subject = "[MMS]임시비밀번호";
			account = accountRepository.findByDeletedAndId(false, item);

			if(ObjectUtils.isNotEmpty(account) && !account.getStatus().equals(3)){

				String str = KeyGenerators.string().generateKey();

				pwd = password.encode(str);

				sb.append("임시비밀번호는 ");
				sb.append(str);
				sb.append(" 입니다.");

			}
		}

		if(ObjectUtils.isNotEmpty(account)){

			if(!account.getStatus().equals(3)){
				sendMail(account.getEmail(), subject, sb.toString());

				if(type.equals("pwd")){

					account.setPassword(pwd);
					account.setStatus(1);
					account.setFail(0);

					accountRepository.save(account);
				}

				map.put("result","success");
			}else{
				map.put("result", "stop");
			}

		}else{
			map.put("result","fail");
		}

		return map;
    }

	public void sendMail(String email, String subject, String content){

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(message, "UTF-8");

			mailHelper.setFrom("tommykim0718@gmail.com");
			mailHelper.setTo(email);
			mailHelper.setSubject(subject);
			mailHelper.setText(content);

			log.info("message ===> {}", message);

			javaMailSender.send(message);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	public String maskId(String id){
		String res = "";
		if (ObjectUtils.isNotEmpty(id)) {
			StringBuilder stringBuilder = new StringBuilder(id);
			res = stringBuilder.replace(2, 4, "**").toString();
		}

		return res;
	}

	public Optional<Account> getProfile(String id) {
		return accountRepository.findById(id);
	}

	public Map<String, Object> checkEmail(String id, String email) {

		map = new HashMap<>();

		// TODO 수정일 경우 본인의 것은 제외하고 체크
		Boolean exist = false;

		if(ObjectUtils.isNotEmpty(id)){
			exist = accountRepository.existsByEmailAndIdNot(email,id);
		}else{
			exist = accountRepository.existsByEmail(email);
		}

		map.put("exist", exist);

		return map;
	}

	@Transactional
	public Map<String, Object> modifyProfile(AccountDTO.General accountDTO) {
		map = new HashMap<>();

		account = new Account();
		account = accountRepository.findByAuth(accountDTO.getAuth());

		try{
			account.setName(accountDTO.getName());
			account.setEmail(accountDTO.getEmail());
			account.setPhone(accountDTO.getPhone());
			if(ObjectUtils.isNotEmpty(accountDTO.getPassword())){
				account.setPassword(password.encode(accountDTO.getPassword()));
			}

			account.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()));

			accountRepository.save(account);

			map.put("result", "success");

		}catch (Exception e){
			map.put("result", "fail");
		}

		return map;
	}
}
