package com.mms.model.converter;

import com.mms.util.SecureUtil;

import javax.persistence.AttributeConverter;

public class MobileConverter implements AttributeConverter<String, String> {


    @Override
    public String convertToDatabaseColumn(String value) {
        try {
            return SecureUtil.encryptMobileNumber(value);
        } catch(Exception e) {
            e.printStackTrace();
            return null ;
        }
    }

    @Override
    public String convertToEntityAttribute(String column) {

        try {
            return SecureUtil.decryptMobileNumber(column);
        } catch(Exception e) {
            e.printStackTrace();
            return null ;
        }
    }
}
