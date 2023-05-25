package com.mms.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ArrayUtils;

public class UploadFile {

	private static final String[] allowedExtension = {".jpg",".png",".bmp",".jpeg",".hwp",".pdf",".doc",".xls",".ppt",".gif",".mp4"};
	private static final String[] badWords = {"./","../","%",";",".\\\\","..\\\\"};
	
	public static String setFile(MultipartFile o_file, String path, String part){
		
		File file=new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
	
		StringBuilder sb = new StringBuilder();
		StringBuilder newFileName = new StringBuilder();
		String orgFilename = o_file.getOriginalFilename();
		
		
		String imsi = "";
		for(String str : badWords) {
			imsi = orgFilename.replaceAll(str, "");
			orgFilename = imsi;
		}
		
		int i = -1;
		
		String now = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
		i = orgFilename.lastIndexOf(".");
		
		String extension = orgFilename.substring(i,orgFilename.length()).toLowerCase();
		
		if(ArrayUtils.contains(allowedExtension, extension)) {
			newFileName.append(part).append(now).append(new Random().nextInt(1000)).append(new Random().nextInt(100)).append(orgFilename.substring(i, orgFilename.length()));

			try {
				o_file.transferTo(new File(path+File.separator+newFileName.toString()));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sb.append(orgFilename);
			sb.append("&");
			sb.append(newFileName);
		}else {
			sb.append(orgFilename);
			sb.append("&");
			sb.append("uploadFail");
		}
		
		
		return sb.toString();
	}
	
	public static String maskWord(final String str) {
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for(char ch:str.toCharArray()){
			if (i < 1) {
				buf.append(ch);
			} else {
				buf.append("");
			}
			i++;
		}
		return buf.toString();
	}
}
