package com.mms.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WordDTO {

    @Data
    public static class General{
        private Long seqNo;
        private String type;
        private String title;
        private String subTitle;
        private String bible;
        private String contents;
        private String serviceDate;

        private List<String> keywordList = new ArrayList<>();
        private List<Image> fileList = new ArrayList<>();
    }

    @Data
    public static class Image{
        private String image;
        private String file;
        private String origin;
    }



}
