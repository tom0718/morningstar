package com.mms.util;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * 암호화를 처리하는 공통 유틸 클래스
 *
 * @author : angelKim
 * @version : 1.0.0
 */
public class SecureUtil {

    private final static byte[] keyBytes = { -20, -96, -100, -20, -99, -76, -19, -120, -84, -20, -105, -108, -20, -89,
            -79, -19 };
    private final static IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
    private final static SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

    /**
     * 비밀번호 단방향 암호화 메소드 (loginId도 반드시 필요)
     *
     * @param loginId
     * @param password
     * @return
     */
    public static String encryptPassword(String loginId, String password) {
        return DigestUtil.encryptSHA256Hmac(loginId + "xwspqj)(#", password + "eptigvy@&^(");
    }


    /**
     * 메소드이름에서 알 수 있듯이 핸드폰번호 전용 암호화 메소드
     *
     * @param mobileNumber
     * @return
     */
    public static String encryptMobileNumber(String mobileNumber) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal((mobileNumber + ".vnkwpq#^&").getBytes("UTF-8"));
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 메소드이름에서 알 수 있듯이 핸드폰번호 전용 복호화 메소드
     *
     * @param mobileNumber
     * @return
     */
    public static String decryptMobileNumber(String mobileNumber) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encrypted = Base64.getDecoder().decode(mobileNumber);
            String decrypted = new String(cipher.doFinal(encrypted), "UTF-8");
            int index = decrypted.indexOf('.');

            return decrypted.substring(0, index);
        } catch (Exception e) {
            return null;
        }
    }

    public static String maskingMobileNumber(String mobileNumber) {
        try {

            StringBuffer sb = new StringBuffer();
            String[] phoneArray = mobileNumber.split("-");

            if(phoneArray.length == 3){
                sb.append(phoneArray[0]);
                sb.append("-");

                sb.append(phoneArray[1].replaceAll("[0-9]","*"));
                sb.append("-");
                sb.append(phoneArray[2]);
            }

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }



}