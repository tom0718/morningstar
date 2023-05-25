package com.mms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connection {

    private Map<String, Map<String,String>> familyTree = new HashMap<>();
    private Map<String, String> familyMap;

    public Connection(){
        setFamilyTree();
    }

    public String getConnectionName(
            String someone, // 입력받은 호칭
            String family, // 가족으로 등록된 호칭
            String gender, // 입력받은 호칭의 성별
            Boolean older
            ){

        /*
            고모, 고모부, 이모, 이모부 가 들어 왔을경우 큰고모, 작은고모 등으로 분기해서 입력
            put("큰고모", "누나");
            put("작은고모", "여동생");
            put("큰고모부", "매형");
            put("작은고모부", "처남");
            put("큰이모", "처형");
            put("작은이모", "처제");
            put("이모부", "동서");
         */

        familyTree.get(someone).get(family);




        return null;
    }

    public String directConnectionName(
            String gender, // 나의 성별
            String someone // 입력받은 호칭
    ){

        String returnName = "";

        switch (someone){
            case "남편" : returnName = "아내"; break;
            case "아내" : returnName = "남편"; break;
            case "아들" : returnName = gender.equals("M") ? "아버지" : "어머니"; break;
            case "딸" : returnName = gender.equals("M") ? "아버지" : "어머니"; break;
            case "할아버지" : returnName = gender.equals("M") ? "손자" : "손녀"; break;
            case "할머니" : returnName = gender.equals("M") ? "손자" : "손녀"; break;
            case "아버지" : returnName = gender.equals("M") ? "아들" : "딸"; break;
            case "어머니" : returnName = gender.equals("M") ? "아들" : "딸"; break;
            case "큰아버지" : returnName = "조카"; break;
            case "큰어머니" : returnName = "조카"; break;
            case "작은아버지" : returnName = "조카"; break;
            case "작은어머니" : returnName = "조카"; break;
            case "고모" : returnName = "조카"; break;
            case "고모부" : returnName = "조카"; break;
            case "이모" : returnName = "외조카"; break;
            case "이모부" : returnName = "외조카"; break;
            case "외삼촌" : returnName = "외조카"; break;
            case "외숙모" : returnName = "외조카"; break;
            case "형" : returnName = "남동생"; break;
            case "누나" : returnName = "남동생"; break;
            case "언니" : returnName = "여동생"; break;
            case "오빠" : returnName = "여동생"; break;
            case "남동생" : returnName = gender.equals("M") ? "형" : "누나"; break;
            case "여동생" : returnName = gender.equals("M") ? "오빠" : "언니"; break;
            case "사촌형" : returnName = "사촌남동생"; break;
            case "사촌누나" : returnName = "사촌남동생"; break;
            case "사촌언니" : returnName = "사촌여동생"; break;
            case "사촌오빠" : returnName = "사촌여동생"; break;
            case "사촌남동생" : returnName = gender.equals("M") ? "사촌형" : "사촌누나"; break;
            case "사촌여동생" : returnName = gender.equals("M") ? "사촌오빠" : "사촌언니"; break;
            case "외사촌형" : returnName = "외사촌남동생"; break;
            case "외사촌누나" : returnName = "외사촌남동생"; break;
            case "외사촌언니" : returnName = "외사촌여동생"; break;
            case "외사촌오빠" : returnName = "외사촌여동생"; break;
            case "외사촌남동생" : returnName = gender.equals("M") ? "외사촌형" : "외사촌누나"; break;
            case "외사촌여동생" : returnName = gender.equals("M") ? "외사촌오빠" : "외사촌언니"; break;
            case "손자" : returnName = gender.equals("M") ? "할어버지" : "할머니"; break;
            case "손녀" : returnName = gender.equals("M") ? "할어버지" : "할머니"; break;
            case "외손자" : returnName = gender.equals("M") ? "외할어버지" : "외할머니"; break;
            case "외손녀" : returnName = gender.equals("M") ? "외할어버지" : "외할머니"; break;

        }

        return returnName;
    }

    public void setFamilyTree(){

        familyTree = new HashMap<>();

        // 아버지
        familyMap = new HashMap<String, String>(){{

            put("할아버지", "아버지");
            put("할머니", "어머니");
            put("외할아버지", "장인");
            put("외할머니", "장모");
            put("어머니", "아내");

            // 상대와의 나이 비교해서 호칭정리
            put("큰고모", "누나");
            put("작은고모", "여동생");
            put("큰고모부", "매형");
            put("작은고모부", "처남");
            put("큰이모", "처형");
            put("작은이모", "처제");
            put("이모부", "동서");

            put("형", "아들");
            put("형수", "며느리");
            put("오빠", "아들");
            put("올케", "며느리");
            put("언니", "딸");
            put("형부", "사위");
            put("누나", "딸");
            put("매형", "사위");

            put("사촌형", "조카");
            put("사촌누나", "조카");
            put("사촌언니", "조카");
            put("사촌오빠", "조카");
            put("사촌남동생", "조카");
            put("사촌여동생", "조카");

            put("외사촌형", "외조카");
            put("외사촌누나", "외조카");
            put("외사촌언니", "외조카");
            put("외사촌오빠", "외조카");
            put("외사촌남동생", "외조카");
            put("외사촌여동생", "외조카");

            // 상대의 성별에 따라 호칭정리
            put("남동생", "아들");
            put("여동생", "딸");
            put("제수", "며느리");
            put("매부", "사위"); // 내가 M 여동생의 남편
            put("제부", "사위"); // 내가 F 여동생의 남편

            put("아들", "손자");
            put("며느리", "손부");
            put("딸", "손녀");
            put("사위", "손서");
            put("손자", "증손자");
            put("손부", "증손부");
            put("손녀", "증손녀");
            put("손서", "증손서");

            put("외손자", "외증손자");
            put("외손녀", "외증손녀");

        }};

        familyTree.put("아버지",familyMap);

        // 어머니
        familyMap = new HashMap<String, String>(){{

        }};

        familyTree.put("어머니",familyMap);


    }

}
