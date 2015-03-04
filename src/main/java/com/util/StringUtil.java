package com.util;

public class StringUtil {

	/**
	 * 首字母大写
	 * 
	 * @param res
	 *            传入字符串
	 * @return 返回首字母大写的res
	 **/
	public static String toUpperCaseFristOne(String res) {
		StringBuffer sb = new StringBuffer(res);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * 首字母小写
	 * 
	 * @param res
	 *            传入字符串
	 * @return 返回首字母小写的res
	 **/
	public static String toLowerCaseFristOne(String res) {
		StringBuffer sb = new StringBuffer(res);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * 驼峰标识
	 * 
	 * @param res
	 *            传入字符串
	 * @return 返回驼峰标识的res
	 **/
	public static String toCamelCase(String res) {
		if (res != null && res.length() > 0) {
			StringBuffer sb = new StringBuffer(res);
			int startIndex = 0;
			int index = sb.substring(startIndex).indexOf("_");
			while (index >= 0) {
				startIndex = index + startIndex + 1;
				if (sb.length() > startIndex) {
					sb.setCharAt(startIndex, Character.toUpperCase(sb
							.charAt(startIndex)));
				}
				index = sb.substring(startIndex).indexOf("_");
			}
			return StringUtil.toLowerCaseFristOne(sb.toString().replaceAll("_",
                    ""));
		}
		return res;
	}

}
