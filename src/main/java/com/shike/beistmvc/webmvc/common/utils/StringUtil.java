package com.shike.beistmvc.webmvc.common.utils;


public class StringUtil {

    public static boolean isEmpty(String str) {
        return (str == null || "".equals(str));
    }

    public static boolean isAllEqual(String str) {
        char[] chars = str.toCharArray();
        if (chars.length == 0) return true;

        char first = chars[0];
        for (int i = 1; i < chars.length; i++) {
            if (first != chars[i]) return false;
        }
        return true;
    }

    public static boolean isStraight(String str) {
        char[] chars = str.toCharArray();
        if (chars.length == 0) return true;

        char cur = chars[0];
        for (int i = 1; i < chars.length; i++) {
            if ((cur + 1) != chars[i]) return false;
            else cur = chars[i];
        }
        return true;
    }

}
