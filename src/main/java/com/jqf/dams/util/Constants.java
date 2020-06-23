package com.jqf.dams.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final int REQUEST_SUCCESS = 0;
    public static final int REQUEST_FAIL = -1;

    public static final String MORE_SELECT = "2";
    public static final String SINGLE_SELECT = "1";

    public static final String RELEASING = "1";
    public static final String NOT_RELEASE = "0";

    public static final String BOY = "男";
    public static final String GIRL = "女";

    public static final String FULL = "1";
    public static final String NOTFULL = "0";

    public static final String NORMAL = "normal";
    public static final String CLUSTRRING = "clustering";

    public static final String A = "1";
    public static final String B = "2";
    public static final String C = "3";
    public static final String D = "4";

    public static final Map<String,String> getAnswerVal = new HashMap<>();

    static {
        getAnswerVal.put("A",Constants.A);
        getAnswerVal.put("B",Constants.B);
        getAnswerVal.put("C",Constants.C);
        getAnswerVal.put("D",Constants.D);
    }

//    static enum AnswerValue{
//         A("1"),B("2"),C("3"),D("4");
//
//         private String value;
//
//        AnswerValue(String s) {
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//    }
}
