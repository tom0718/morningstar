package com.mms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

@Component("downloadFile")
public class DownloadFile extends AbstractView{

	@Override
	protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		
		String fileName = map.get("file").toString();
		
        String path = (String) map.get("path");
        String downName = (String) map.get("fileName");
        
        File file = new File(path, fileName);

        String extension = fileName.substring(fileName.lastIndexOf("."));
        
        res.setContentType(getContentType());
        res.setContentLength((int)file.length());
        setResponse(req,res,downName+extension);
        res.setHeader("Content-Transfer-Encoding", "binary");
         
        OutputStream out = res.getOutputStream();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) { try { fis.close(); } catch (Exception e2) {}}
        }
        out.flush();
        
	}
	
	private void setResponse(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException{
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5 이하
            response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
        } else if (userAgent.indexOf("MSIE") > -1) { // MS IE (보통은 6.x 이상 가정)
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
        } else if (userAgent.indexOf("Trident") > -1) { // MS IE 11
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
        } else { // 모질라나 오페라
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1").replaceAll("\\+", "\\ ") + ";");
        }
    }

}
