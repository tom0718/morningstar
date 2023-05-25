package com.mms.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class FunctionUtil {

	protected static final Predicate<MultipartFile> nullCheck = f -> f != null && !f.isEmpty();
	protected static final TriFunction<MultipartFile, String, String, String> setFile = UploadFile::setFile;
	protected static final BiConsumer<String, String> deleteFile = DeleteFile::delFile;
	protected static final String key = Common.AES_KEY;
	protected static final Function<String, String> checkInput = XssMyFilter::checkNormal;
	protected static final Function<String, String> checkHtml = XssMyFilter::checkScript;
	protected static final Function<String, String> setID = SetID::getID;
	
	protected static final String SessionName = "mms";

    protected static final List<String> jobItem = new ArrayList<String>(){
        {
            add("법무");
            add("공무원");
            add("의학");
            add("교육");
            add("사업");
            add("자영업");
            add("서비스");
            add("건축");
            add("미디어");
            add("IT");
            add("학생");
            add("주부");
            add("기타");
        }
    };

    protected static final List<String> starItem = new ArrayList<String>(){
        {
            add("스타1차");
            add("스타2차");
            add("명솔");
            add("주빛나리1차");
            add("주빛나리2차");
            add("주희망");
            add("비혼(주소망)");
        }
    };

    protected static final List<String> specialUnitItem = new ArrayList<String>(){
        {
            add("행복사");
            add("썬스타");
            add("예술단");
            add("사사부");
            add("의학부");
            add("개우지");
            add("기타");
        }
    };

	
	
}
