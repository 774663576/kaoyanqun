/**
 * 工程名: KyGroup
 * 文件名: StringUtil.java
 * 包名: com.edu.kygroup.utils
 * 日期: 2013-10-27上午10:01:25
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.utils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 类名: StringUtil 功能: TODO 字符串处理工具类. 日期: 2013-10-27 上午10:01:25
 * 
 * @author lx
 * @version
 */
public class StringUtils {
	private static final String TAG = "StringUtil";

	/*
	 * 判断字符串为空
	 */
	public static boolean isEmpty(String str) {
		boolean ret = false;
		if (TextUtils.isEmpty(str) || str.equals("null") || str.equals("NULL")) {
			ret = true;
		}
		return ret;
	}

	/**
	 * 判断给定字符串是否空白串�?br> 空白串是指由空格、制表符、回车符、换行符组成的字符串<br>
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isBlank(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 去掉字符串首尾的空格 判断字符串是否为空，为空直接返回，不为空去掉空格后返回
	 */
	public static String trim(String str) {
		if (isNullOrEmpty(str))
			return str;
		return str.trim();
	}

	/**
	 * 去掉字符串首尾的空格及全角空格 判断字符串是否为空，为空直接返回，不为空去掉空格后返回
	 */
	public static String trim2(String str) {
		if (isNullOrEmpty(str))
			return str;
		return trimFilling(trimFilling(str.trim(), '　', false), '　', true);
	}

	/**
	 * 去除字串中的填充字符
	 */
	public static String trimFilling(String str, char filling,
			boolean leftJustified) {
		if (str == null || str.length() == 0)
			return str;
		if (leftJustified) {
			int pos = str.length() - 1;
			for (; pos >= 0; pos--) {
				if (str.charAt(pos) != filling)
					break;
			}
			return str.substring(0, pos + 1);
		}
		int pos = 0;
		for (; pos < str.length(); pos++) {
			if (str.charAt(pos) != filling)
				break;
		}
		return str.substring(pos);
	}

	/**
	 * 将一个字符串分解成几个段
	 */
	public static String[] split(String str, int segLen, int segNum) {
		String[] result = new String[segNum];
		if (str == null || str.length() == 0)
			return result;

		byte[] strByte;
		try {
			strByte = str.getBytes("GBK");
		} catch (UnsupportedEncodingException ex) {
			strByte = str.getBytes();
		}
		int pos = 0;
		for (int i = 0; i < segNum; i++) {
			int actLen = ((strByte.length - pos) < segLen) ? (strByte.length - pos)
					: segLen;
			byte[] b = new byte[actLen];
			System.arraycopy(strByte, pos, b, 0, actLen);
			result[i] = new String(b);
			pos += actLen;
			if (pos >= strByte.length)
				break;
		}
		return result;
	}

	/**
	 * 得到以head开头,tail结尾的子串
	 */
	public static String subString(String buffer, String head, String tail) {
		if (buffer == null || head == null || tail == null)
			return buffer;
		int startNum = buffer.indexOf(head);
		int endNum = buffer.lastIndexOf(tail);

		startNum = startNum >= 0 ? startNum : 0;
		endNum = endNum >= 0 ? endNum + tail.length() : buffer.length();
		return buffer.substring(startNum, endNum);
	}

	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof String)
			return ((String) obj).trim().length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			boolean empty = true;
			for (int i = 0; i < object.length; i++)
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			return empty;
		}
		return false;
	}

	/**
	 * 将用separator分隔的String转化为List，如果str中没有separator则返回的List中只有 str一项
	 */
	public static List str2List(String str, String separator) {
		List result = new ArrayList();
		if (str.indexOf(separator) < 0) {
			result.add(str);
		} else {
			String[] strArray = str.split(separator);

			for (int i = 0; i < strArray.length; i++) {
				result.add(strArray[i]);
			}
		}
		return result;
	}

	/**
	 * 数据库建表用
	 */
	public static String replaceTrim(String str) {
		if (str != null) {
			return "tab" + str.replace("@", "_").replace(".", "");
		} else {
			return "";
		}
	}

	/**
	 * 字符串是否全数字（无符号、小数点）
	 */
	public static boolean isDigit(String str) {
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c < '0' || c > '9')
				return false;
		}
		return true;
	}

	/**
	 * 字符串是否全数字或英文字母（无符号、小数点）
	 */
	public static boolean isAlphaDigit(String str) {
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c < '0' || c > '9' && c < 'A' || c > 'Z' && c < 'a' || c > 'z')
				return false;
		}
		return true;
	}

	/**
	 * 按千位分割格式格式化数字
	 */
	public static String parseStringPattern(Object v, int scale) {
		String temp = "###,###,###,###,###,###,###,##0";
		if (scale > 0)
			temp += ".";
		for (int i = 0; i < scale; i++)
			temp += "0";
		DecimalFormat format = new DecimalFormat(temp);
		return format.format(v).toString();
	}

	/**
	 * 转为String[]
	 * 
	 * @param object
	 * @return
	 */
	public static String[] getStringArray(Object object) {
		if (object instanceof String[])
			return (String[]) object;
		if (object instanceof String) {
			String tmpStrs[] = new String[1];
			tmpStrs[0] = (String) object;
			return tmpStrs;
		}
		return null;
	}

	/**
	 * 取得类的简单名
	 */
	public static String getSimpleName(Object obj) {
		if (obj == null)
			return null;
		String name = obj.getClass().getName();
		if (name.toLowerCase().indexOf("$proxy") >= 0) {
			name = obj.toString();
			int idx = name.lastIndexOf("@");
			if (idx > 0)
				name = name.substring(0, idx);
		}
		return name.substring(name.lastIndexOf(".") + 1);
	}

	/**
	 * 截字串
	 */
	public static String subString(String str, int length) {
		if (str == null) {
			return "";
		}
		int end = str.length();
		if (length > end) {
			end = length;
		}
		return str.substring(0, end);
	}

	/**
	 * 获取字符串的长度, 如果为null则长度为0
	 */
	public static int length(String str) {
		if (isNullOrEmpty(str))
			return 0;
		else
			return str.trim().length();

	}

	public static String stringOf(Date date) {
		return stringOf(TimeZone.getDefault(), date);
	}

	public static String stringOf(TimeZone timeZone, Date date) {
		if (date == null) {
			return "null";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		if (timeZone != null) {
			formatter.setTimeZone(timeZone);
		}
		return formatter.format(date);
	}

	/**
	 * 判断邮箱合法性
	 */
	public static boolean isMailCorrect(String mail) {
		Pattern p = Pattern
				.compile("[a-zA-Z0-9-_]+@+[a-zA-Z0-9]+.+[a-zA-Z0-9]");
		Matcher m = p.matcher(mail);
		return m.matches();
	}
}
