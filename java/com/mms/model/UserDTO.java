package com.mms.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    @Data
    public static class general{

        private Long seqNo;
        private String code; // 교회코드
        private String realName; // 본명
        private String reName; // 개명
        private String thumbnail;
        private String birthday; // 생일
        private String gender; // 성별
        private String blood; // 혈액형
        private Long departmentSeqNo; // 부서

        private String completionDate; // 수료일
        private String phone;
        private String mission; // 사명
        private String religiousLevel; // 신급
        private Integer secondGeneration; // 2세여부 1:가정국2세, 2:장년부2세, 3:2세아님.
        private String star; // 스타
        private String specialUnit; // 특수부서
        private Integer theology; // 신학기수
        private String earlierChurch; // 이전교회
        private String transferDate; // 전입일

        private String school; // 졸업학교
        private String major; // 전공
        private String job; // 직업
        private String company; // 회사명
        private String talent; // 특기
        private String feature; // 특징

        private List<String> licenseList = new ArrayList<>(); // 자격증

        private String address; // 주소
        private String credit; // 공적
        private String note; // 비고


        private MultipartFile thumbnailFile;

        private List<String> familyList = new ArrayList<>();

        private List<Providence> providenceList = new ArrayList<>();

    }

    @Data
    public static class Providence{
        private String title;
        private String name;
    }
}
