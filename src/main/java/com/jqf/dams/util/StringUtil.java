package com.jqf.dams.util;

public class StringUtil {

    public static boolean isEmpty(String str){
        if("".equals(str) || str == null){
            return true;
        }else {
            return false;
        }
    }

    public static boolean equals(String str,String str2){
        if(str2 == str){
            return true;
        }else {
            return false;
        }
    }

    public static void main(String[] args) {
        String a = "Development of allocation dormitory system based on clustering algorithm";
        System.out.println(a.toUpperCase());
//        System.out.println(a.substring(0,a.length()-1));
    }

}
