package com.mms.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SessionUser {

    private String id;
    private String code;
    private String auth;
    private String part; // staff, pastor, admin, supreme, word, manager
    private String name;


    private List<AccountRole> roleList = new ArrayList<>(); // department, gender

}
