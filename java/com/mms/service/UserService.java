package com.mms.service;

import com.mms.model.*;
import com.mms.repository.*;
import com.mms.util.Connection;
import com.mms.util.FunctionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class UserService extends FunctionUtil {

    private Map<String, Object> map;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    private User user;
    private License license;
    private Counseling counseling;


    private User userMe;
    private User userSomeone;
    private SearchUser searchUser;

    private Relation relation;

    private Connection connection;

    private List<Relation> relationList;

    private Family family;
    private ProvidenceFamily providenceFamily;

    private Calendar today;

    private Date startDate;
    private Date endDate;
    private Long temp;
    private String[] genderArray;
    private List<User> userList;

    private List<Predicate> predicates;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private CounselingRepository counselingRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private ProvidenceFamilyRepository providenceFamilyRepository;

    @Autowired
    private SearchUserRepository searchUserRepository;

    @Transactional
    public void regist(UserDTO.general dto, SessionUser sessionUser, String path) throws Exception {

        user = new User();

        Date today = Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant());

        if(ObjectUtils.isNotEmpty(dto.getSeqNo())){ // 수정시
            user = userRepository.findBySeqNo(dto.getSeqNo());

            user.setModiDate(today);
            user.setUpdater(sessionUser.getId());


        }else{ // 등록시

            if(sessionUser.getPart().equals("supreme")){
                user.setCode(dto.getCode());
            }else{
                user.setCode(sessionUser.getCode());
            }

            user.setSecure(KeyGenerators.string().generateKey());

            user.setRegDate(today);
            user.setRegister(sessionUser.getId());

            user.setDeleted(false);
        }



        if(!dto.getThumbnailFile().isEmpty()){
            String returnName = setFile.apply(dto.getThumbnailFile(), path, "T");
            String[] fileNameList = returnName.split("&");
            user.setThumbnail("/repository/"+fileNameList[1]);
        }else{
            user.setThumbnail(dto.getThumbnail());
        }

        user.setRealName(dto.getRealName());
        user.setReName(dto.getReName());
        user.setPhone(dto.getPhone());
        user.setBirthday(sf.parse(dto.getBirthday()));
        user.setGender(dto.getGender());
        if(ObjectUtils.isNotEmpty(dto.getBlood())){
            user.setBlood(dto.getBlood());
        }else{
            user.setBlood(null);
        }
        user.setDepartmentSeqNo(dto.getDepartmentSeqNo());
        user.setMission(dto.getMission());
        user.setReligiousLevel(dto.getReligiousLevel());

        if(ObjectUtils.isNotEmpty(dto.getCompletionDate())){
            user.setCompletionDate(sf.parse(dto.getCompletionDate()));
        }else{
            user.setCompletionDate(null);
        }

        user.setStar(dto.getStar());
        user.setSpecialUnit(dto.getSpecialUnit());
        user.setTheology(dto.getTheology());
        user.setEarlierChurch(dto.getEarlierChurch());

        if(ObjectUtils.isNotEmpty(dto.getTransferDate())){
            user.setTransferDate(sf.parse(dto.getTransferDate()));
        }else{
            user.setTransferDate(null);
        }

        user.setSecondGeneration(dto.getSecondGeneration());

        //user.setFamilyRelation(dto.getFamilyRelation());
        //user.setProvidenceFamily(dto.getProvidenceFamily());

        user.setSchool(dto.getSchool());
        user.setMajor(dto.getMajor());
        user.setJob(dto.getJob());
        user.setCompany(dto.getCompany());
        user.setTalent(dto.getTalent());
        user.setFeature(dto.getFeature());
        user.setAddress(dto.getAddress());
        user.setCredit(dto.getCredit());
        user.setNote(dto.getNote());

        user = userRepository.save(user);

        licenseRepository.deleteByUser(user.getSeqNo());

        // license 저장
        if(ObjectUtils.isNotEmpty(dto.getLicenseList())){
            dto.getLicenseList().forEach(l -> {
                license = new License();
                license.setUser(user.getSeqNo());
                license.setName(l);

                licenseRepository.save(license);
            });
        }

        familyRepository.deleteByUserSeqNo(user.getSeqNo());

        // 가족관계
        if(ObjectUtils.isNotEmpty(dto.getFamilyList())){

            dto.getFamilyList().forEach(f -> {

                family = new Family();
                family.setUserSeqNo(user.getSeqNo());
                family.setTitle(f);

                familyRepository.save(family);
            });
        }

        providenceFamilyRepository.deleteByUserSeqNo(user.getSeqNo());

        //섭리가족
        if(ObjectUtils.isNotEmpty(dto.getProvidenceList())){

            dto.getProvidenceList().forEach(f -> {

                providenceFamily = new ProvidenceFamily();
                providenceFamily.setUserSeqNo(user.getSeqNo());
                providenceFamily.setTitle(f.getTitle());
                providenceFamily.setName(f.getName());

                providenceFamilyRepository.save(providenceFamily);
            });
        }


    }

    public Page<User> getUserList(Pageable pageable, SessionUser sessionUser, String code, String name, Long department, String gender, String star, String specialUnit, String job, Integer age) {
        @SuppressWarnings("serial")
        Specification<User> specification = new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

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

                    if (!star.equals("")) {
                        predicates.add(builder.equal(root.get("star"), star));
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

                if (!specialUnit.equals("")) { // 특수부서
                    predicates.add(builder.equal(root.get("specialUnit"), specialUnit));
                }

                if (!job.equals("")) { // 직업
                    predicates.add(builder.equal(root.get("job"), job));
                }

                if (ObjectUtils.isNotEmpty(age)) { // 연령

                    today = new GregorianCalendar();
                    Integer year = today.get(Calendar.YEAR) - age + 1;

                    String start = year + "-01-01";
                    String end = year + "-12-31";

                    startDate = new Date();
                    endDate = new Date();
                    try {
                        startDate = sf.parse(start);
                        endDate = sf.parse(end);

                        predicates.add(builder.between(root.get("birthday"), startDate, endDate));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                query.where(predicates.toArray(new Predicate[] {}));

                return builder.and(predicates.toArray(new Predicate[] {}));
            }
        };

        return userRepository.findAll(specification, pageable);
    }

    public User getUser(String secure) {
        return userRepository.findBySecure(secure);
    }

    public Map<String, Object> saveCounseling(Long userSeqNo, Long seqNo, String title, String content, String handling, String counselor) {

        map = new HashMap<>();

        counseling = new Counseling();

        Date today = Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant());

        if(ObjectUtils.isNotEmpty(seqNo)){
            counseling = counselingRepository.findBySeqNo(seqNo);

            counseling.setUpdateDate(today);
            counseling.setUpdater(counselor);
        }else{
            counseling.setRegDate(today);
            counseling.setRegister(counselor);
            counseling.setUserSeqNo(userSeqNo);
        }

        counseling.setTitle(title);
        counseling.setContent(content);
        counseling.setHandling(handling);

        counselingRepository.save(counseling);


        return map;
    }

    public Page<Counseling> getCounselingList(Pageable pageable, SessionUser sessionUser, Long userSeqNo) {
        return counselingRepository.findByUserSeqNo(pageable, userSeqNo);
    }

    public Map<String, Object> getCounseling(Long seqNo) {
        map = new HashMap<>();
        map.put("counseling", counselingRepository.findBySeqNo(seqNo));
        return map;
    }

    @Transactional
    public Map<String, Object> deleteCounseling(Long seqNo) {
        map = new HashMap<>();

        counselingRepository.deleteBySeqNo(seqNo);

        return map;
    }

    @Transactional
    public Map<String, Object> removeRelationship(Long me, Long userSeqNo) {
        map = new HashMap<>();

        // 나와 상대 모두에게서 삭제
        relationRepository.deleteByMeAndFamily(me, userSeqNo);
        relationRepository.deleteByMeAndFamily(userSeqNo, me);

        return map;
    }

    @Transactional
    public Map<String, Object> deleteUser(Long seqNo, String updater) {
        map = new HashMap<>();

        user = new User();
        user = userRepository.findBySeqNo(seqNo);

        // TODO setting
        user.setDeleted(true);
        user.setDeletedDate(Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()));
        user.setUpdater(updater);

        userRepository.save(user);

        // TODO 나와 연결된 가족관계도 삭제필요.
        relationList = new ArrayList<>();
        relationList = relationRepository.findByMe(seqNo);

        // 나와 연결된 가족과 상대의 입장 모두 저장
        relationList.forEach(r -> {
            relationRepository.deleteByFamily(r.getMe());
        });

        relationRepository.deleteByMe(seqNo);

        return map;
    }

    @Transactional
    public Map<String, Object> saveRelationship(Long me, Long userSeqNo, String someone) {
        map = new HashMap<>();

        connection = new Connection();

        userMe = new User();
        userMe = userRepository.findBySeqNo(me);

        userSomeone = new User();
        userSomeone = userRepository.findBySeqNo(userSeqNo);


        // TODO 입력된 관계 저장, 나와 입장, 상대의 입장 모두 저장
        // 나의 입장
        relation = new Relation();
        relation.setMe(me);
        relation.setFamily(userSeqNo);
        relation.setName(someone);

        relationRepository.save(relation);

        // 상대의 입장
        relation = new Relation();
        relation.setMe(userSeqNo);
        relation.setFamily(me);
        relation.setName(connection.directConnectionName(userMe.getGender(), someone));

        relationRepository.save(relation);

        // TODO 직접 연결만 등록을 할 것인지, 가족으로 등록된 사람들 모두를 등록할지?
        /*

        // 나와 연결된 가족 목록 가져오기
        relationList = new ArrayList<>();
        relationList = relationRepository.findByMe(me);

        // 나와 연결된 가족과 상대의 입장 모두 저장
        relationList.forEach(r -> {

            relation = new Relation();
            relation.setMe(r.getFamily());
            relation.setFamily(userSeqNo);


            String strName = connection.getConnectionName(someone, r.getName(), userSomeone.getGender(), true); //
            relation.setName(strName);

            relationRepository.save(relation);




        });

        // TODO 상대와 연결된 가족이 있는지 체크

         */


        
        












        return map;
    }

    public Page<User> connectionList(Pageable pageable, SessionUser sessionUser, Long me, Long department, String gender, String userName) {


        // 가족관계 목록
        relationList = new ArrayList<>();
        relationList = relationRepository.findByMe(me);

        @SuppressWarnings("serial")
        Specification<User> specification = new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                predicates = new ArrayList<>();

                predicates.add(builder.equal(root.get("deleted"), false));

                // 나와
                predicates.add(builder.notEqual(root.get("seqNo"), me));

                // 이미 가족으로 등록된 인원제외
                if(ObjectUtils.isNotEmpty(relationList)){
                    relationList.forEach(f -> {
                        predicates.add(builder.notEqual(root.get("seqNo"), f.getFamily()));
                    });
                }

                // 같은 교회내에서만 검색
                predicates.add(builder.equal(root.get("code"), sessionUser.getCode()));

                if(!department.equals(0l)){
                    predicates.add(builder.equal(root.get("departmentSeqNo"), department));
                }

                if (!gender.equals("")) {
                    String[] arr = gender.split(",");
                    predicates.add(builder.in(root.get("gender")).value(Arrays.asList(arr)));
                }

                if(!userName.equals("")){
                    predicates.add(builder.or(builder.like(root.get("realName"), "%" + userName + "%"), builder.like(root.get("reName"), "%" + userName + "%")));
                }

                query.where(predicates.toArray(new Predicate[] {}));

                return builder.and(predicates.toArray(new Predicate[] {}));
            }
        };

        return userRepository.findAll(specification, pageable);
    }

    public List<Relation> getRelationList(Long seqNo) {

        @SuppressWarnings("serial")
        Specification<Relation> specification = new Specification<Relation>() {

            @Override
            public Predicate toPredicate(Root<Relation> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                predicates = new ArrayList<>();

                Join<Relation, User> join = root.join("relation");

                predicates.add(builder.equal(root.get("me"), seqNo));
                predicates.add(builder.equal(join.get("deleted"), false));

                query.where(predicates.toArray(new Predicate[] {}));

                return builder.and(predicates.toArray(new Predicate[] {}));
            }
        };

        return relationRepository.findAll(specification);
    }

    public String getThumbnail(Long id) {
        return userRepository.getThumbnail(id);
    }

    public User getUserBySeqNo(Long id) {
        return userRepository.findBySeqNo(id);
    }

    public void convertPhoneNumber() {

        userList = new ArrayList<>();
        userList = userRepository.findAll();

        userList.forEach(user -> {

            //user.setMobileNumber(user.getPhone());

            //userRepository.save(user);

        });

    }

    public Page<SearchUser> getSearchUserList(Pageable pageable, SessionUser sessionUser, String code, String name, Long department, String gender) {

        Specification<SearchUser> specification = new Specification<SearchUser>() {

            @Override
            public Predicate toPredicate(Root<SearchUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                predicates = new ArrayList<>();

                predicates.add(builder.equal(root.get("deleted"), false));

                if(sessionUser.getPart().equals("supreme")){
                    if(!code.equals("")){
                        predicates.add(builder.equal(root.get("code"), code));
                    }
                }else{
                    predicates.add(builder.equal(root.get("code"), sessionUser.getCode()));
                }


                if(!department.equals(0l)){
                    predicates.add(builder.equal(root.get("departmentSeqNo"), department));
                }

                if (!gender.equals("")) {
                    String[] arr = gender.split(",");
                    predicates.add(builder.in(root.get("gender")).value(Arrays.asList(arr)));
                }

                if(!name.equals("")){ // 본명 또는 개명 검색
                    predicates.add(builder.or(builder.like(root.get("realName"), "%" + name + "%"), builder.like(root.get("reName"), "%" + name + "%")));
                }

                query.where(predicates.toArray(new Predicate[] {}));

                return builder.and(predicates.toArray(new Predicate[] {}));
            }
        };

        return searchUserRepository.findAll(specification, pageable);
    }

    public SearchUser getSearchUser(String secure) {
        return searchUserRepository.findBySecure(secure);
    }

    public Optional<SearchUser> checkMember(String name, String phone, String secure, String mode) {
        map = new HashMap<>();

        @SuppressWarnings("serial")
        Specification<SearchUser> specification = new Specification<SearchUser>() {

            @Override
            public Predicate toPredicate(Root<SearchUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                predicates = new ArrayList<>();

                if(mode.equals("checkView")){
                    predicates.add(builder.equal(root.get("secure"), secure));
                }

                predicates.add(builder.equal(root.get("deleted"), false));

                // 같은 교회내에서만 검색
                predicates.add(builder.equal(root.get("code"), "S0001"));
                predicates.add(builder.equal(root.get("phone"), phone));
                predicates.add(builder.or(builder.like(root.get("realName"), "%" + name + "%"), builder.like(root.get("reName"), "%" + name + "%")));


                query.where(predicates.toArray(new Predicate[] {}));

                return builder.and(predicates.toArray(new Predicate[] {}));
            }
        };

        return searchUserRepository.findOne(specification);

    }

    public Map<String, Object> changeProfileImage(MultipartFile image, String secure, String path) {
        map = new HashMap<>();

        try{
            String returnName = setFile.apply(image, path, "T");
            String[] fileNameList = returnName.split("&");

            String profile = "/repository/"+fileNameList[1];

            searchUser = new SearchUser();
            searchUser = searchUserRepository.findBySecure(secure);

            String oldImg = searchUser.getThumbnail().replace("repository/","");

            searchUser.setThumbnail(profile);

            searchUserRepository.save(searchUser);

            String deleteImage = path + oldImg;

            log.info("deleteImageName ======> {}", deleteImage);

            File file=new File(deleteImage);

            if(file.exists()){
                file.delete();
                log.info("file delete success =======");
            }else{
                log.info("file delete fail =======");
            }

            map.put("result", "success");
            map.put("img", profile);

        }catch (Exception e){
            log.error(e.getMessage());
            map.put("result", "fail");
        }

        return map;
    }
}
