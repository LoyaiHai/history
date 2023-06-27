package com.lhs.toollibrary.utils;

public class HS_StringUtils {

    public static boolean isBlank(String content) {
        int strLen;
        if (content != null && (strLen = content.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                //判断字符给定的字符(Unicode 代码点)是否为空白字符
                if (!Character.isWhitespace(content.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }



}
