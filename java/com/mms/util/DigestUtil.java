package com.mms.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class DigestUtil {

    public static String encodeBase64(String str) {
        byte[] encodeByte = Base64.getEncoder().encode(str.getBytes());
        return new String(encodeByte);
    }

    public static String decodeBase64(String str) {
        byte[] decodeByte = Base64.getDecoder().decode(str.getBytes());
        return new String(decodeByte);
    }

    public static String encryptMD5(String str) {
        String MD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte byteData[] = md.digest();
            MD5 = NumberUtil.bytesToHexString(byteData);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }

    public static String encryptSHA1(String str) {
        String SHA1 = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA1");
            sh.update(str.getBytes("UTF-8"));
            byte[] digest = sh.digest();
            SHA1 = NumberUtil.bytesToHexString(digest);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            SHA1 = null;
        }

        return SHA1;
    }

    public static String encryptSHA256(String str) {
        String SHA256 = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes("UTF-8"));
            byte byteData[] = sh.digest();
            SHA256 = NumberUtil.bytesToHexString(byteData);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            SHA256 = null;
        }
        return SHA256;
    }

    public static String encryptSHA256Hmac(String deviceNumber) {
        return DigestUtil.encryptSHA256Hmac("^__^;*!!", deviceNumber);
    }

    public static String encryptSHA256Hmac(String secretKey, String deviceNumber) {

        String SHA256 = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] sha256_result = sha256_HMAC.doFinal(deviceNumber.getBytes());
            SHA256 = NumberUtil.bytesToHexString(sha256_result);

        } catch (Exception e) {
            e.printStackTrace();
            SHA256 = null;
        }
        return SHA256;
    }
}
