package com.example.drop.wzjtestapp.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: StringUtils 
 * @Description: String工具类
 * @author liang_xs
 */
public class StringUtils {
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    
	 public static boolean isEmpty(CharSequence cs) {
	        return cs == null || cs.length() == 0;
	    }
	 
		public static boolean notEmpty(String s){
			return s!=null && !"".equals(s) && !"null".equals(s);
		}

    /**
     * 获得第一个分隔字符串前的所有字符
     * @param str 原字符串
     * @param separator 分隔字符串
     * @return
     */
    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }
    /**
     * 获得第一个分隔字符串后的所有字符
     * @param str 原字符串
     * @param separator 分隔字符串
     * @return
     */
    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }
    /**
     * 获得最后一个分隔字符串前的所有字符
     * @param str 原字符串
     * @param separator 分隔字符串
     * @return
     */
    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 获得最后一个分隔字符串后的所有字符
     * @param str 原字符串
     * @param separator 分隔字符串
     * @return
     */
    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND || pos == (str.length() - separator.length())) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 获得分隔字符中间的字符串
     * @param str 原字符串
     * @param tag 分隔字符串
     * @return
     */
    public static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag);
    }

    /**
     * 获得不同分隔字符中间的字符串
     * @param str 原字符串
     * @param open 分隔字符串开始
     * @param close 分隔字符串结束
     * @return
     */
    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND) {
            int end = str.indexOf(close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * 获得不同分隔字符中间的字符串数组
     * @param str 原字符串
     * @param open 分隔字符串开始
     * @param close 分隔字符串结束
     * @return
     */
    public static String[] substringsBetween(String str, String open, String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return EMPTY_STRING_ARRAY;
        }
        int closeLen = close.length();
        int openLen = open.length();
        List<String> list = new ArrayList<String>();
        int pos = 0;
        while (pos < (strLen - closeLen)) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }
}
