package com.jqf.dams.util;

import java.util.Date;

public class GenerateIdUtil {

    public static String generateId(){
        String id = DateUtil.getNowTimeStr();
        return id;
    }
}
