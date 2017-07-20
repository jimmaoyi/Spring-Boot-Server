package com.maryun.utils;

import org.apache.commons.lang.StringUtils;

public class CommUtil {
	/**
	 * 将逗号分隔的字段，转化成in字符串，例1,2,3 --》 '1','2','3'
	 * @param arg
	 * @return
	 */
	public static String arr2InStr(String arg) {
		String in = "";
		if (StringUtils.isNotBlank(arg)) {
			String[] as = arg.split(",");
			for (int i = 0; i < as.length; i++) {
				in += "'" + as[i].trim() + "',";
			}
		}
		if (!"".equals(in)) {
			in = in.substring(0, in.length() - 1);
		}
		return in;
	}

	public static String obj2Str(Object obj) {
		if (null != obj) {
			return obj.toString();
		}
		else {
			return "";
		}
	}
}
