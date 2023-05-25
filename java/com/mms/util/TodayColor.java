package com.mms.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TodayColor {

    private static List<String> colorList = new ArrayList<String>(){{
        add("#F5A9A9");
        add("#2E9AFE");
        add("#FA58D0");
        add("#3ADF00");
        add("#D358F7");
        add("#FFBF00");
        add("#81DAF5");
        add("#DF7401");
        add("#FA5882");
        add("#9FF781");
    }};

    public static String getTodayColor(){

        LocalDate today = LocalDate.now();
        long day = today.getDayOfMonth();
        day = day - 1;
        String color = "";
        if(day < 10){
            color = colorList.get((int) day);
        }else if(day < 20){
            color = colorList.get((int) day / 20);
        }else if(day < 30){
            color = colorList.get((int) day / 30);
        }else{
            color = colorList.get(5);
        }

        return color;
    }

}
