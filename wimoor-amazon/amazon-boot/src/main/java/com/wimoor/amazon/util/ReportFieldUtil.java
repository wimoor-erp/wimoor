package com.wimoor.amazon.util;

import java.math.BigDecimal;
import java.util.Map;

import com.wimoor.common.GeneralUtil;

public class ReportFieldUtil {

	public static String getStrValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			return info[position];
		}
		return null;
	}
	
	public static BigDecimal getBigDecimalValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			String value=info[position];
			value = value.replaceAll("[^\\d.]", "");
			if(GeneralUtil.isNumericzidai(value)) {
				return new BigDecimal(value);
			}
		}
		return null;
	}

	public static Integer getIntegerValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			if(GeneralUtil.isNumericzidai(info[position])) {
				return   Integer.parseInt(info[position]);
			}
		}
		return null;
	}
	
	public static boolean getBooleanValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return false;
		if(position<info.length && info[position].equals("true")) {
			return true;
		} else {
			return false;
		}
	}
}
