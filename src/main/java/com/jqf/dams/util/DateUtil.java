package com.jqf.dams.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getNowTimeStr(){
        Format format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date());
    }

}
