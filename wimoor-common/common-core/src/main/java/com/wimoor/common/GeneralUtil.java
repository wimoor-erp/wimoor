package com.wimoor.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.util.StrUtil;
 

/**
 * 将对象转换为json格式字符串
 * @author felix_liu
 * @version 2016-11-05
 * @param Object
 * @return json string
 */

public class GeneralUtil {
	public static final SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 此功能可以将数据库里面查出来的list<map> 结构在进行以某个字段进行分类。
	 * 
	 * @param list
	 * @param by
	 * @return
	 */
	public static boolean isEmpty(String value){
		return value==null||value.trim().length()==0;
	}
	
	   public static Map<String, Object> objectToMap(Object obj)  {    
	        if(obj == null){    
	            return null;    
	        }   
	  
	        Map<String, Object> map = new HashMap<String, Object>();    
	  
	        Field[] declaredFields = obj.getClass().getDeclaredFields();    
	        for (Field field : declaredFields) {    
	            field.setAccessible(true);  
	            try {
					map.put(field.getName(), field.get(obj));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
	        }    
	  
	        return map;  
	    }  
	public static File getFileByBytes(byte[] bytes, String filePath, String fileName, String suffix) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			if (isEmpty(filePath)) {
				file = File.createTempFile(fileName, suffix);
			} else {
				File dir = new File(filePath);
				if (!dir.exists() && dir.isDirectory()) {
					dir.mkdirs();
				}
				file = new File(filePath + "\\" + fileName);
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public static Map<String, ArrayList<Map<String, Object>>> groupListMapBy(List<Map<String, Object>> list, String by) {
		Map<String, ArrayList<Map<String, Object>>> map = new TreeMap<String, ArrayList<Map<String, Object>>>(Collections.reverseOrder());
		if (list == null) {
			return null;
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapitem = list.get(i);
			if (!mapitem.containsKey(by)) {
				continue;
			}
			String authid = mapitem.get(by).toString();
			if (!isEmpty(authid)) {
				if (map.get(authid) == null) {
					ArrayList<Map<String, Object>> item = new ArrayList<Map<String, Object>>();
					item.add(mapitem);
					map.put(authid, item);
				} else {
					ArrayList<Map<String, Object>> item = map.get(authid);
					item.add(mapitem);
					map.put(authid, item);
				}
			}
		}
		return map;
	}
	/**
	 * 判断是否是json结构
	 */
	public static JSONObject getJsonObject(String value) {
		if(value==null)return null;
		try {
			return JSONObject.parseObject(value);
		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * 判断是否是json结构
	 */
	public static JSONArray getJsonArray(String value) {
		if(value==null)return null;
		try {
			return JSONArray.parseArray(value);
		} catch (JSONException e2) {
			return null;
		}
	}
	public static String toJSON(Object obj) {
		if (obj == null) {
			return null;
		}
		ObjectMapper om = new ObjectMapper();
		try {
			String json = om.writeValueAsString(obj);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static <T> T parse(String jsonStr, Class<T> clazz) {
		ObjectMapper om = new ObjectMapper();
		T readValue = null;
		try {
			readValue = om.readValue(jsonStr, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readValue;
	}

 
	// 判断是否是今天
	public static boolean isNow(Date date) {
		Date now = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String nowDay = sf.format(now);
		String day = sf.format(date);
		return day.equals(nowDay);
	}

	/**
	 * 不容许含有SQL相关代码
	 * 
	 * @param param
	 * @return
	 */
	public static boolean checkParamSql(String param) {
		if (param.contains("select") || param.contains("show") || param.contains("delete") || param.contains("update")
				|| param.contains("drop") || param.contains("create")) {
			return false;
		}
		return true;
	}
   public static  Date getFirstDayofWeek(Date time) {
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(time);
		    // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		    int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		    if (1 == dayWeek) {
		        cal.add(Calendar.DAY_OF_MONTH, -1);
		    }
		    cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		    int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		    cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		    return  cal.getTime();
		}
   
   public static  Date getDayofWeek(Date time,int num) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(time);
	    // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
	    int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
	    if ( dayWeek<=num) {
	        cal.add(Calendar.DAY_OF_MONTH, -1*num);
	    }
	    cal.setFirstDayOfWeek(num+1);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
	    int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
	    cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
	    return  cal.getTime();
	}
     
	public static  List<String> getListParam(String value) {
		if(value==null||value.trim().length()==0||"all".equals(value)) {
			return null;
		}else {
			ArrayList<String> list = new ArrayList<String>();
			String[] grouparray = value.split(",");
			for(int i=0;i<grouparray.length;i++) {
				String item = grouparray[i];
				if(item!=null&&item.trim().length()>0) {
					list.add(item);
				}
			}
			if(list.size()>0) {
				return list;
			}
			return null;
	}
    }
	/**
	 * 从指定的URL导入JSON供网络数据解析用
	 * 
	 * @param url
	 * @return
	 */
	public static String loadJson(String url) {
		StringBuilder json = new StringBuilder();
		BufferedReader in = null;
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json.toString();
	}

	static int node = 0;
	static List<Map<String, String>> listcookies = null;

	public static Map<String, String> getCookies(int i) {
		if (listcookies == null) {
			listcookies = new ArrayList<Map<String, String>>();
			Map<String, String> cookies0 = new HashMap<String, String>();
			cookies0.put("session-id", "257-7849104-1304152");
			cookies0.put("session-id-time", "2082758401l");
			cookies0.put("ubid-acbuk", "260-1676826-4278825");
			cookies0.put("monitor_count", "1");
			cookies0.put("x-wl-uid", "1dr8W+gnWhncI1pe9fwJEUxVgOqJ/FRHMqLnn4D/3/kZx0N4f73mriJIIwMOgCpL3QWTlkadKG3c=");
			cookies0.put("csm-hit", "tb:35PDBWWMBAVX6WZSYZQA+s-35PDBWWMBAVX6WZSYZQA|1544926752251&t:1544926752251&adb:adblk_no");
			cookies0.put("session-token",
					"WF1jNp5ZuFcw1AvsR5zI2oXhbHM/Iw+LZD1bAeU4SqJhrGyuNLKoRi6p6CjrToJH+UYLXWQwVH0BDR5ZWpB4XADee9ID9Sw6HN49plc2FBTN8q0lj4PJ5PHBaLrFcbU+4rTEqPY6CoBg+i5AtO2uIxvw1yXPNTGiPsPzAEj+KqYQFI1o9CG4l6TosQm/O6xAE0IwSYIJRun3Q2ziNuNIp9HW+gk1USbqu/iPMpOTl8a9mcucs1JbU0l2+NrJjOFBn+FlvnvSYRs=");
			Map<String, String> cookies1 = new HashMap<String, String>();
			cookies1.put("session-id", "257-7849104-1304152");
			cookies1.put("session-id-time", "2082758401l");
			cookies1.put("ubid-acbuk", "260-1676826-4278825");
			cookies1.put("monitor_count", "2");
			cookies1.put("x-wl-uid", "1dr8W+gnWhncI1pe9fwJEUxVgOqJ/FRHMqLnn4D/3/kZx0N4f73mriJIIwMOgCpL3QWTlkadKG3c=");
			cookies1.put("csm-hit", "tb:35PDBWWMBAVX6WZSYZQA+s-35PDBWWMBAVX6WZSYZQA|1544926752251&t:1544926752251&adb:adblk_no");
			cookies1.put("session-token",
					"WF1jNp5ZuFcw1AvsR5zI2oXhbHM/Iw+LZD1bAeU4SqJhrGyuNLKoRi6p6CjrToJH+UYLXWQwVH0BDR5ZWpB4XADee9ID9Sw6HN49plc2FBTN8q0lj4PJ5PHBaLrFcbU+4rTEqPY6CoBg+i5AtO2uIxvw1yXPNTGiPsPzAEj+KqYQFI1o9CG4l6TosQm/O6xAE0IwSYIJRun3Q2ziNuNIp9HW+gk1USbqu/iPMpOTl8a9mcucs1JbU0l2+NrJjOFBn+FlvnvSYRs=");
			Map<String, String> cookies2 = new HashMap<String, String>();
			cookies2.put("session-id", "257-7849104-1304152");
			cookies2.put("session-id-time", "2082758401l");
			cookies2.put("ubid-acbuk", "260-1676826-4278825");
			cookies2.put("monitor_count", "3");
			cookies2.put("x-wl-uid", "1dr8W+gnWhncI1pe9fwJEUxVgOqJ/FRHMqLnn4D/3/kZx0N4f73mriJIIwMOgCpL3QWTlkadKG3c=");
			cookies2.put("csm-hit", "tb:35PDBWWMBAVX6WZSYZQA+s-35PDBWWMBAVX6WZSYZQA|1544926752251&t:1544926752251&adb:adblk_no");
			cookies2.put("session-token",
					"WF1jNp5ZuFcw1AvsR5zI2oXhbHM/Iw+LZD1bAeU4SqJhrGyuNLKoRi6p6CjrToJH+UYLXWQwVH0BDR5ZWpB4XADee9ID9Sw6HN49plc2FBTN8q0lj4PJ5PHBaLrFcbU+4rTEqPY6CoBg+i5AtO2uIxvw1yXPNTGiPsPzAEj+KqYQFI1o9CG4l6TosQm/O6xAE0IwSYIJRun3Q2ziNuNIp9HW+gk1USbqu/iPMpOTl8a9mcucs1JbU0l2+NrJjOFBn+FlvnvSYRs=");
			Map<String, String> cookies3 = new HashMap<String, String>();
			cookies3.put("session-id", "257-7849104-1304152");
			cookies3.put("session-id-time", "2082758401l");
			cookies3.put("ubid-acbuk", "260-1676826-4278825");
			cookies3.put("monitor_count", "4");
			cookies3.put("x-wl-uid", "1dr8W+gnWhncI1pe9fwJEUxVgOqJ/FRHMqLnn4D/3/kZx0N4f73mriJIIwMOgCpL3QWTlkadKG3c=");
			cookies3.put("csm-hit", "tb:35PDBWWMBAVX6WZSYZQA+s-35PDBWWMBAVX6WZSYZQA|1544926752251&t:1544926752251&adb:adblk_no");
			cookies3.put("session-token",
					"WF1jNp5ZuFcw1AvsR5zI2oXhbHM/Iw+LZD1bAeU4SqJhrGyuNLKoRi6p6CjrToJH+UYLXWQwVH0BDR5ZWpB4XADee9ID9Sw6HN49plc2FBTN8q0lj4PJ5PHBaLrFcbU+4rTEqPY6CoBg+i5AtO2uIxvw1yXPNTGiPsPzAEj+KqYQFI1o9CG4l6TosQm/O6xAE0IwSYIJRun3Q2ziNuNIp9HW+gk1USbqu/iPMpOTl8a9mcucs1JbU0l2+NrJjOFBn+FlvnvSYRs=");
			Map<String, String> cookies4 = new HashMap<String, String>();
			cookies4.put("session-id", "257-7849104-1304152");
			cookies4.put("session-id-time", "2082758401l");
			cookies4.put("ubid-acbuk", "260-1676826-4278825");
			cookies4.put("monitor_count", "1");
			cookies4.put("x-wl-uid", "1dr8W+gnWhncI1pe9fwJEUxVgOqJ/FRHMqLnn4D/3/kZx0N4f73mriJIIwMOgCpL3QWTlkadKG3c=");
			cookies4.put("csm-hit", "tb:35PDBWWMBAVX6WZSYZQA+s-35PDBWWMBAVX6WZSYZQA|1544926752251&t:1544926752251&adb:adblk_no");
			cookies4.put("session-token",
					"WF1jNp5ZuFcw1AvsR5zI2oXhbHM/Iw+LZD1bAeU4SqJhrGyuNLKoRi6p6CjrToJH+UYLXWQwVH0BDR5ZWpB4XADee9ID9Sw6HN49plc2FBTN8q0lj4PJ5PHBaLrFcbU+4rTEqPY6CoBg+i5AtO2uIxvw1yXPNTGiPsPzAEj+KqYQFI1o9CG4l6TosQm/O6xAE0IwSYIJRun3Q2ziNuNIp9HW+gk1USbqu/iPMpOTl8a9mcucs1JbU0l2+NrJjOFBn+FlvnvSYRs=");

			listcookies.add(cookies0);
			listcookies.add(cookies1);
			listcookies.add(cookies2);
			listcookies.add(cookies3);
			listcookies.add(cookies4);
		}
		return listcookies.get(i);
	}

	public static Date StringfromDate(String argsp, String type) {
		SimpleDateFormat formatter = new SimpleDateFormat(type);
		Date date = null;
		try {
			date = formatter.parse(argsp.trim());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date Dateformatter(Date date, String type) {
		SimpleDateFormat formatter = new SimpleDateFormat(type);
		String str = formatter.format(date);
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date utc2LocalDate(String utcTime, String utcTimePatten) {
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return gpsUTCDate;
	}

	public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
	}

	public static String getStrDate(Date date) {
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
		return sdf4.format(date);
	}

	public static Date getDateNoTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0, 0);
		return c.getTime();
	}

	public static String formatCurrency(String currency) {
		String fcuurency = null;
		if (currency != null) {
			if (currency.equalsIgnoreCase("USD")) {
				fcuurency = "$";
			}
			if (currency.equalsIgnoreCase("GBP")) {
				fcuurency = "£";
			}
			if (currency.equalsIgnoreCase("EUR")) {
				fcuurency = "€";
			}
			if (currency.equalsIgnoreCase("JPY")) {
				fcuurency = "¥";
			}
			if (currency.equalsIgnoreCase("CNY")) {
				fcuurency = "¥";
			}
			if (currency.equalsIgnoreCase("RMB")) {
				fcuurency = "¥";
			}
			if (currency.equalsIgnoreCase("CAD")) {
				fcuurency = "C$";
			}
			if (currency.equalsIgnoreCase("INR")) {
				fcuurency = "₹";
			}
			if (currency.equalsIgnoreCase("AUD")) {
				fcuurency = "A$";
			}
			if (currency.equalsIgnoreCase("MXN")) {
				fcuurency = "Mex$";
			}
			if (currency.equalsIgnoreCase("SEK")) {
				fcuurency = "Kr";
			}
			if (currency.equalsIgnoreCase("SAR")) {
				fcuurency = "S$";
			}
			if (currency.equalsIgnoreCase("AED")) {
				fcuurency = "AE$";
			}
			if (currency.equalsIgnoreCase("PLN")) {
				fcuurency = "zł";
			}
			if (currency.equalsIgnoreCase("TRY")) {
				fcuurency = "₺";
			}
			
		}
		return fcuurency;
	}
	
	public static String getMarketname(String mcurrency) {
		String marketname = null;
		if (mcurrency != null) {
			if("USD".equals(mcurrency)) {
				marketname = "美国";
			}else if("CAD".equals(mcurrency)) {
				marketname = "加拿大";
			}else if("GBP".equals(mcurrency)) {
				marketname = "英国";
			}else if("INR".equals(mcurrency)) {
				marketname = "印度";
			}else if("JPY".equals(mcurrency)) {
				marketname = "日本";
			}else if("AUD".equals(mcurrency)) {
				marketname = "澳大利亚";
			}else if("MXN".equals(mcurrency)) {
				marketname = "墨西哥";
			}else if("EUR".equals(mcurrency)) {
				marketname = "欧洲（不包含英国）";
			}else if("AED".equals(mcurrency)) {
				marketname = "阿联酋";
			}else if("SAR".equals(mcurrency)) {
				marketname = "沙特阿拉伯";
			}else if("PLN".equals(mcurrency)) {
				marketname = "波兰";
			}else if("SEK".equals(mcurrency)) {
				marketname = "瑞典";
			}
		}
		return marketname;
	}
	
	public static String getMarketPlaceId(String mcurrency) {
		String marketname = null;
		if (mcurrency != null) {
			if("USD".equals(mcurrency)) {
				marketname = "ATVPDKIKX0DER";
			}else if("CAD".equals(mcurrency)) {
				marketname = "A2EUQ1WTGCTBG2";
			}else if("GBP".equals(mcurrency)) {
				marketname = "A1F83G8C2ARO7P";
			}else if("INR".equals(mcurrency)) {
				marketname = "A21TJRUUN4KGV";
			}else if("JPY".equals(mcurrency)) {
				marketname = "A1VC38T7YXB528";
			}else if("AUD".equals(mcurrency)) {
				marketname = "A39IBJ37TRP1C6";
			}else if("MXN".equals(mcurrency)) {
				marketname = "A1AM78C64UM0Y8";
			}else if("EUR".equals(mcurrency)) {
				marketname = "EU";
			}else if("AED".equals(mcurrency)) {
				marketname = "A2VIGQ35RCS4UG";
			}else if("SEK".equals(mcurrency)) {
				marketname = "A2NODRKZP88ZB9";
			}else if("PLN".equals(mcurrency)) {
				marketname = "A1C3SOZRARQ6R3";
			}else if("SAR".equals(mcurrency)) {
				marketname = "A17E79C6D8DWNP";
			}
		}
		return marketname;
	}

	

	public static String removeLastZero(float amount) {// 如果最後一位数是0，去掉最后一位0,保留2位小数
		String result = amount + "";
		DecimalFormat df = new DecimalFormat("######0.00");
		if (amount > 0) {
			if (result.contains(".") && result.substring(result.length() - 1) == "0") {
				try {
					result = df.parse(result) + "";
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

 
 
	public static String formatterQuantity(float number) {// 对浮点型数进行格式化，保留2位小数，如：1,000.00
		return outputInt((int) (Math.floor(number)) + "") + outputcents(number);
	}
	public static String formatterQuantity(BigDecimal number) {// 对浮点型数进行格式化，保留2位小数，如：1,000.00
		number = number.setScale(2, RoundingMode.HALF_UP);
		String num = number.toString();
		String[] numarray = num.split("\\.");
		String intnum = numarray[0];
		String fot = null;
		int i = 0;
		if (numarray.length >= 2) {
			fot = numarray[1];
			if (StrUtil.isNotEmpty(fot)) {
				char[] array = fot.toCharArray();
				for (i = array.length - 1; i >= 0; i--) {
					if (array[i] != '0')
						break;
				}
			}
		}
		String dotstr = ".00";
		if (fot != null && fot.length() > 0 && i >= 0) {
			dotstr = "." + fot.substring(0, i + 1);
		}
		return outputInt(intnum) + dotstr;
	}

 

	public static String outputInt(String number) {// 对整数进行格式化，如：1,000
		String intnum = null;
		boolean flag = false;
		if (number.startsWith("-")) {
			flag = true;
			number = number.substring(1);
		}
		if (number.length() <= 3) {
			intnum = (number == "" ? "0" : number);
		} else {
			int mod = number.length() % 3;
			String output = (mod == 0 ? "" : (number.substring(0, mod)));
			for (int i = 0; i < Math.floor(number.length() / 3); i++) {
				if ((mod == 0) && (i == 0)) {
					output += number.substring(mod + 3 * i, mod + 3 * i + 3);
				} else {
					output += ',' + number.substring(mod + 3 * i, mod + 3 * i + 3);
				}
			}
			intnum = output;
		}
		if (flag) {
			return "-" + intnum;
		} else {
			return intnum;
		}
	}

	public static String outputcents(float amount) {
		int tempamount = (int) Math.round((amount - Math.floor(amount)) * 100);
		return tempamount < 10 ? ".0" + tempamount : "." + tempamount;
	}
	
	public static Date getDate(Object o) {
		if(o==null) {
			   return null;
		}else if(o instanceof Date) {
			    return (Date)o;
		}else if(o instanceof Long) {
			    return new Date((Long)o);
		}else {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
			try {
				return fmt.parse(o.toString());
			} catch (ParseException e) {
				SimpleDateFormat fmt2 =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
				// TODO Auto-generated catch block
				try {
					return fmt2.parse(o.toString());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					return GeneralUtil.getDatez(o.toString());
				}
			}
			
			
		}
	}

	public static Date getDatez(String date) {
		if (isEmpty(date)) {
			return null;
		}
		date = date.replace("/", "-");
		date = date.replace("UTC", "");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
		SimpleDateFormat sdf3 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		SimpleDateFormat sdf4 = new SimpleDateFormat("dd.MM.yyyy");
	
		SimpleDateFormat sdf6 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf7 = new SimpleDateFormat("yyyyMMdd");
		try {
			return sdf1.parse(date.trim());
		} catch (ParseException e) {
			try {
				return sdf2.parse(date);
			} catch (ParseException e2) {
				try {
					return sdf3.parse(date);
				} catch (ParseException e3) {
					try {
						return sdf4.parse(date);
					} catch (ParseException e4) {
						try {
							SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd");
							if(date.length()>12) {
								if(date.contains("T")) {
									sdf5 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
								}else {
									sdf5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								}
							}
							return sdf5.parse(date);
						} catch (ParseException e5) {
							try {
								return sdf6.parse(date);
							} catch (ParseException e6) {
								try {
									return sdf7.parse(date);
								} catch (ParseException e7) {
									return new Date(Long.parseLong(date));
								} 
							} // catch4
							
						} // catch4
					} // catch4
				} // catch3
			} // catch2
		} // catch1
	}

	// "2016-11-03T11:04:20+00:00"
	public static Date getDatePlus(String date, int plus) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			Calendar c = Calendar.getInstance();
			String date2 = date.substring(0, 19);
			if (date2 != null) {
				c.setTime(sdf.parse(date2.trim()));
				c.add(Calendar.HOUR, 8);
			}
			return c.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getDatePlusByZone(String date, String country) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if ("US".equals(country) || "CA".equals(country)) {
			format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		} else if ("UK".equals(country)) {
			format.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		} else if ("FR".equals(country) || "IT".equals(country) || "ES".equals(country) || "DE".equals(country)) {
			format.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		} else if ("JP".equals(country)) {
			format.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		} else if ("IN".equals(country)) {
			format.setTimeZone(TimeZone.getTimeZone("Asia/NEW DELHI"));
		} else if ("AU".equals(country)) {
			format.setTimeZone(TimeZone.getTimeZone("Australia/Canberra"));
		}
		try {
			String date2 = date.substring(0, 19);
			if (date2 != null) {
				return format.parse(date2.trim());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getDatePlus(Calendar c,String market) {
		if ("US".equals(market) || "CA".equals(market)) {
			c.add(Calendar.HOUR, -8);
			if(isSummerTime(c,market)){
				c.add(Calendar.HOUR, 1);//夏时令
			}
		} else if("UK".equals(market)){
			if(isSummerTime(c,market)){
				c.add(Calendar.HOUR, 1);//夏时令
			}
		} else if ("IN".equals(market)) {
			c.add(Calendar.HOUR, 5);
			c.add(Calendar.MINUTE, 30);
		} else if ("AU".equals(market)) {
			c.add(Calendar.HOUR, 2);
		} else if ("JP".equals(market)) {
			c.add(Calendar.HOUR, 9);
		} else if("NL".equals(market)||"PL".equals(market)||"SE".equals(market)||"DE".equals(market)||"FR".equals(market)||"ES".equals(market)||"IT".equals(market)) {
			c.add(Calendar.HOUR, 1);
			if(isSummerTime(c,market)){
				c.add(Calendar.HOUR, 1);//夏时令
			}
		}else if("AE".equals(market)) {
			c.add(Calendar.HOUR, 4); 
		}else if("SA".equals(market)) {
			c.add(Calendar.HOUR, 3); 
		}else if("BR".equals(market)) {
			c.add(Calendar.HOUR, -3); 
		}else if("TR".equals(market)) {
			c.add(Calendar.HOUR, 3); 
		}else if("EG".equals(market)) {
			c.add(Calendar.HOUR, 2); 
		}else if("MX".equals(market)) {
			c.add(Calendar.HOUR, -6);
		}
		return c.getTime();
	
	}
	
	public static String getCountryDateToUTC(String date,String market) {
		Date oDate = GeneralUtil.getDatez(date);
		Calendar c=Calendar.getInstance();
		c.setTime(oDate);
		oDate=GeneralUtil.getCountryDateToUTC(c, market);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return sdf1.format(oDate);
	}
	public static Date getCountryDateToUTC(Calendar c,String market) {
		if ("US".equals(market) || "CA".equals(market)) {
			c.add(Calendar.HOUR, 8);
			if(isSummerTime(c,market)){
				c.add(Calendar.HOUR, -1);//夏时令
			}
		} else if("UK".equals(market)){
			if(isSummerTime(c,market)){
				c.add(Calendar.HOUR, -1);//夏时令
			}
		} else if ("IN".equals(market)) {
			c.add(Calendar.HOUR, -5);
			c.add(Calendar.MINUTE, -30);
		} else if ("AU".equals(market)) {
			c.add(Calendar.HOUR, -2);
		} else if ("JP".equals(market)) {
			c.add(Calendar.HOUR, -9);
		} else if("NL".equals(market)||"PL".equals(market)||"SE".equals(market)||"DE".equals(market)||"FR".equals(market)||"ES".equals(market)||"IT".equals(market)) {
			c.add(Calendar.HOUR, -1);
			if(isSummerTime(c,market)){
				c.add(Calendar.HOUR, -1);//夏时令
			}
		}else if("AE".equals(market)) {
			c.add(Calendar.HOUR, -4); 
		}else if("SA".equals(market)) {
			c.add(Calendar.HOUR, -3); 
		}else if("BR".equals(market)) {
			c.add(Calendar.HOUR, 3); 
		}else if("TR".equals(market)) {
			c.add(Calendar.HOUR, -3); 
		}else if("EG".equals(market)) {
			c.add(Calendar.HOUR, -2); 
		}else if("MX".equals(market)) {
			c.add(Calendar.HOUR, 6);
		}
		return c.getTime();
	
	}
	public static Date getDatePlus(String date, String market) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Calendar c = Calendar.getInstance();
		try {
			if(date==null||date.length()==0)return null;
			String date2 = date.substring(0, 19);
			if (date2 != null) {
				c.setTime(sdf.parse(date2.trim()));
				return GeneralUtil.getDatePlus(c, market);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isSummerTime(Calendar c, String market) {
		boolean flag = false;
		Calendar end = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		// 美国夏令时一般在3月第二个周日凌晨2AM（当地时间）开始,在11月第一个周日凌晨2AM（当地时间）夏令时结束
		if("US".equals(market)||"CA".equals(market)){
			start.set(c.get(Calendar.YEAR), 2, 1, 2, 0, 0);// 2020-03-01
			if (start.get(Calendar.DAY_OF_WEEK) == 1) {// 星期日
				start.add(Calendar.DAY_OF_MONTH, 7);
			} else {
				start.add(Calendar.DAY_OF_MONTH, 8 - start.get(Calendar.DAY_OF_WEEK) + 7);
			}
			
			end.set(c.get(Calendar.YEAR), 10, 1, 2, 0, 0);// 2020-11-01
			if (end.get(Calendar.DAY_OF_WEEK) > 1) {// 如果不是星期日
				end.add(Calendar.DAY_OF_MONTH, 8 - start.get(Calendar.DAY_OF_WEEK));
			}
		}
		//每年三月份的最后一个周日2:00（格林尼治时间）开始，至十月份最后一个周日 3:00（格林尼治时间）结束。
		if("UK".equals(market)||"DE".equals(market)||"FR".equals(market)||"IT".equals(market)
				||"ES".equals(market)||"PL".equals(market)||"SE".equals(market)){
			start.set(c.get(Calendar.YEAR), 2, 31, 2, 0, 0);// 2020-03-31
			if (start.get(Calendar.DAY_OF_WEEK) > 1) {// 星期日
				start.add(Calendar.DAY_OF_MONTH, 1 - start.get(Calendar.DAY_OF_WEEK));
			}
			
			end.set(c.get(Calendar.YEAR), 9, 31, 3, 0, 0);// 2020-10-31
			if (end.get(Calendar.DAY_OF_WEEK) > 1) {// 如果不是星期日
				end.add(Calendar.DAY_OF_MONTH, 1 - start.get(Calendar.DAY_OF_WEEK));
			}
		}

		if ((c.compareTo(start) == 0 || c.after(start)) && c.before(end)) {
			flag = true;
		}
		return flag;
	}
 

 

	public static String removeRearComma(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}
		if (str.trim().charAt(str.length() - 1) == ',') {
			str = str.trim().substring(0, str.length() - 1);
		}
		return str;
	}

	public static String encodeStr(String str) {
		try {
			if (str == null) {
				return null;
			}
			if (str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
				return new String(str.getBytes("ISO-8859-1"), "UTF-8");
			} else {
				return str;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String[] getColumnNames(Object wage) {
		java.lang.reflect.Field[] field = wage.getClass().getDeclaredFields();// 这里便是获得实体Bean中所有属性的方法
		StringBuffer sb = new StringBuffer();
		String[] wageStrArray = null;
		for (int i = 0; i < field.length; i++) {// 这里不多说了
			sb.append(field[i].getName());
			// 这是分割符 是为了去掉最后那个逗号
			// 比如 如果不去最后那个逗号 最后打印出来的结果是 "id,name,"
			// 去了以后打印出来的是 "id,name"
			if (i < field.length - 1) {
				sb.append(",");
			}
		}
		// split(",");这是根据逗号来切割字符串 使字符串变成一个数组
		wageStrArray = sb.toString().split(",");
		return wageStrArray;
	}

	public static String getSundayOfThisWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		c.add(Calendar.DATE, -day_of_week + 7);
		return formatDate(c.getTime(), FMT_YMD);
	}

	public static String formatDate(Date date, SimpleDateFormat sdf) {
		return sdf.format(date);
	}

	public static String formatDate(Date date, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(date);
	}

	public static String formatDate(Date date) {
		if(date==null) {
			return null;
		}
		return FMT_YMD.format(date);
	}

	public static Date getNextDate(Date data) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime();
	}

	public static Date getYesterday(Date data) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.add(Calendar.DATE, -1);// 把日期往后减一天.整数往后推,负数往前移动
		return calendar.getTime();
	}
	
	public static Date getSevenDayBefore(Date data) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.add(Calendar.DATE, -7);// 把日期往后减一天.整数往后推,负数往前移动
		return calendar.getTime();
	}
	
	public static Date getLastMonthday(Date data) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}
	
	public static Date getFirstdayOfMonth(Date data) {
		Calendar calendar = new GregorianCalendar();
		Calendar calendar2 = new GregorianCalendar();
		calendar.setTime(data);
		calendar2.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1);
		return calendar2.getTime();
	}
	
	public static Date getLastdayOfMonth(Date data) {
		Calendar calendar = new GregorianCalendar();
		Calendar calendar2 = new GregorianCalendar();
		calendar.setTime(data);
		calendar2.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,1);
		calendar2.add(Calendar.DAY_OF_MONTH, -1);
		return calendar2.getTime();
	}

	public static boolean isBeforeToday(String dateStr) {
		Date date = getDatez(dateStr);
		Date today = new Date();
		String todaystr = getStrDate(today);
		today = getDatez(todaystr);
		return date.before(today);
	}

	public static double distanceOfHour(Calendar cal, Calendar today) {
		if (cal.after(today)) {
			return Math.floor((cal.getTimeInMillis() - today.getTimeInMillis()) / 1000 / 3600);
		} else {
			return Math.floor((today.getTimeInMillis() - cal.getTimeInMillis()) / 1000 / 3600);
		}
	}
	public static double distanceOfHour(Date pcal, Date ptoday) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(pcal);
		Calendar today=Calendar.getInstance();
		today.setTime(ptoday);
		if (cal.after(today)) {
			return Math.floor((cal.getTimeInMillis() - today.getTimeInMillis()) / 1000 / 3600);
		} else {
			return Math.floor((today.getTimeInMillis() - cal.getTimeInMillis()) / 1000 / 3600);
		}
	}
	public static double distanceOfMinutes(Date cal, Date today) {
		if(cal==null||today==null)return 0.0;
		if (cal.after(today)) {
			return Math.floor((cal.getTime() - today.getTime()) / 1000 / 60);
		} else {
			return Math.floor((today.getTime() - cal.getTime()) / 1000 / 60);
		}
	}

	public static double distanceOfSecond(Date cal, Date today) {
		if (cal.after(today)) {
			return Math.floor((cal.getTime() - today.getTime()) / 1000);
		} else {
			return Math.floor((today.getTime() - cal.getTime()) / 1000);
		}
	}
	public static double distanceOfDay(Date cal, Date today) {
		if (today.before(cal)) {
			return Math.floor((cal.getTime() - today.getTime()) / 1000 / 3600 / 24);
		} else {
			return Math.floor((today.getTime() - cal.getTime()) / 1000 / 3600 / 24);
		}
	}
	public static double distanceOfDay(Calendar cal, Calendar today) {
		if (cal.before(today)) {
			return Math.floor((cal.getTimeInMillis() - today.getTimeInMillis()) / 1000 / 3600 / 24);
		} else {
			return Math.floor((today.getTimeInMillis() - cal.getTimeInMillis()) / 1000 / 3600 / 24);
		}
	}

 
 
 

 

 
	/**
	 * 取Bean的属性和值对应关系的MAP
	 * 
	 * @param bean
	 * @return Map
	 */
	public static Map<String, String> getFieldValueMap(Object bean) {
		Class<?> cls = bean.getClass();
		Map<String, String> valueMap = new HashMap<String, String>();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldType = field.getType().getSimpleName();
				String fieldGetName = parGetName(field.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
				String result = null;
				if ("Date".equals(fieldType)) {
					result = fmtDate((Date) fieldVal);
				} else {
					if (null != fieldVal) {
						result = String.valueOf(fieldVal);
					}
				}
				valueMap.put(field.getName(), result);
			} catch (Exception e) {
				continue;
			}
		}
		return valueMap;
	}

	/**
	 * 取Bean的属性和值对应关系的MAP
	 * 
	 * @param bean
	 * @return Map
	 */
	public static Map<String, Object> getFieldObjectMap(Object bean) {
		Class<?> cls = bean.getClass();
		Map<String, Object> valueMap = new HashMap<String, Object>();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				//String fieldType = field.getType().getSimpleName();
				String fieldGetName = parGetName(field.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
				valueMap.put(field.getName(), fieldVal);
			} catch (Exception e) {
				continue;
			}
		}
		return valueMap;
	}

	/**
	 * set属性的值到Bean
	 * 
	 * @param bean
	 * @param valMap
	 */

	public static void setFieldValue(Object bean, Map<String, String> valMap) {
		Class<?> cls = bean.getClass();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldSetName = parSetName(field.getName());
				if (!checkSetMet(methods, fieldSetName)) {
					continue;
				}
				Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
				String value = valMap.get(field.getName());
				if (null != value && !"".equals(value)) {
					String fieldType = field.getType().getSimpleName();
					if ("String".equals(fieldType)) {
						fieldSetMet.invoke(bean, value);
					} else if ("Date".equals(fieldType)) {
						Date temp = parseDate(value);
						fieldSetMet.invoke(bean, temp);
					} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
						Integer intval = Integer.parseInt(value);
						fieldSetMet.invoke(bean, intval);
					} else if ("Long".equalsIgnoreCase(fieldType)) {
						Long temp = Long.parseLong(value);
						fieldSetMet.invoke(bean, temp);
					} else if ("Double".equalsIgnoreCase(fieldType)) {
						Double temp = Double.parseDouble(value);
						fieldSetMet.invoke(bean, temp);
					} else if ("Boolean".equalsIgnoreCase(fieldType)) {
						Boolean temp = Boolean.parseBoolean(value);
						fieldSetMet.invoke(bean, temp);
					} else {
						System.out.println("not supper type" + fieldType);
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
	}

	/**
	 * 格式化string为Date
	 * 
	 * @param datestr
	 * @return date
	 */
	public static Date parseDate(String datestr) {
		if (null == datestr || "".equals(datestr)) {
			return null;
		}
		try {
			String fmtstr = null;
			if (datestr.indexOf(':') > 0) {
				fmtstr = "yyyy-MM-dd HH:mm:ss";
			} else {
				fmtstr = "yyyy-MM-dd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(fmtstr);
			return sdf.parse(datestr);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 日期转化为String
	 * 
	 * @param date
	 * @return date string
	 */
	public static String fmtDate(Date date) {
		if (null == date) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断是否存在某属性的 set方法
	 * 
	 * @param methods
	 * @param fieldSetMet
	 * @return boolean
	 */
	private static boolean checkSetMet(Method[] methods, String fieldSetMet) {
		for (Method met : methods) {
			if (fieldSetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否存在某属性的 get方法
	 * 
	 * @param methods
	 * @param fieldGetMet
	 * @return boolean
	 */
	private static boolean checkGetMet(Method[] methods, String fieldGetMet) {
		for (Method met : methods) {
			if (fieldGetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 拼接某属性的 get方法
	 * 
	 * @param fieldName
	 * @return String
	 */
	private static String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	/**
	 * 拼接在某属性的 set方法
	 * 
	 * @param fieldName
	 * @return String
	 */
	private static String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	public static String getStr(Object object) {
		if (object == null) {
			return null;
		}
		return object.toString();
	}
	/*
	 * 判断是否为浮点数，包括double和float
	 * 
	 * @param str 传入的字符串
	 * 
	 * @return 是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	/*
	 * 判断是否为整数
	 * 
	 * @param str 传入的字符串
	 * 
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static String getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最小天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = sdf.format(cal.getTime());
		return firstDayOfMonth;
	}

	/**
	 * 获得该月最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = 31;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			lastDay = 31;
		} else {
			if (month == 2) {
				if (year % 4 == 0 || year % 400 == 0) {
					lastDay = 29;
				} else {
					lastDay = 28;
				}
			} else {
				lastDay = 30;
			}
		}
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}
	
	public static int getDayNumOfMonth(int year, int month) {
		// 获取某月最大天数
		int lastDay = 31;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			lastDay = 31;
		} else {
			if (month == 2) {
				if (year % 4 == 0 || year % 400 == 0) {
					lastDay = 29;
				} else {
					lastDay = 28;
				}
			} else {
				lastDay = 30;
			}
		}
		return lastDay;
	}

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
			if(isNumericzidai(info[position])) {
				return new BigDecimal(info[position]);
			}
		}
		return null;
	}
	public static Integer getIntegerValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			if(isNumericzidai(info[position])) {
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
	
	public static boolean isNumericzidai(String str) {
		if (str == null)
			return false;
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	public static String getIndexString(String[] info, int i) {
		if (info.length > i) {
			String value = info[i];
			return value;
		}
		return null;
	}
	
 
 

	/**
	 * 判断是否是xml结构
	 */
	public static Document getXML(String value) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			return builder.parse(value);
		} catch (ParserConfigurationException e) {
			return null;
		} catch (SAXException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	public static String addParamToUrl(String url, Map<String, Object> param, String fieldname) {
		if (param.get(fieldname) != null) {
			url = url + ("".equals(url) ? "" : "&");
			url = url + fieldname + "=" + param.get(fieldname);
		}
		return url;
	}

	@SuppressWarnings("unchecked")
	public static String addParamToUrl2(String url, Map<String, Object> param, String fieldname) {
		if (param.get(fieldname) != null) {
			url = url + ("".equals(url) ? "" : "&;");
			Object object=param.get(fieldname);
			if(object instanceof List) {
				List<String> list = (List<String>)object;
				for(String item:list) {
					url = url + ("".equals(url) ? "" : "&;");
					url = url + fieldname + "=" +item ;
				}
			}else {
				url = url + fieldname + "=" +object.toString() ;
			}
			
		}
		return url;
	}

 
	 
	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @param nowTime   当前时间
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 * @author jqlin
	 */
	public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
			return true;
		}
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);
		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getRandom() {
		char[] ary = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		Random random = new Random();
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (int i = 0; i < 6; i++) {
			int number = random.nextInt(36);
			while (hashSet.contains(number)) {
				number = random.nextInt(36);
			}
			hashSet.add(number);
		}
		String result = "";
		for (Integer i : hashSet) {
			result = result + ary[i];
		}
		return result;
	}

	public static boolean checkExpression(String expression) {
		char t, tt = expression.charAt(0);
		int sum = 0, add = 0; // 计算左右括号,计算修正位数
		boolean hasNum = false;
		StringBuffer s = new StringBuffer(expression);
		if (tt == '+' || tt == '-' || tt == '.') {
			s.insert(0, '0');
			add++;
		} 
		else if (tt == ')' || tt == '*' || tt == '/') {
			return false;
		}
		for (int i = 0; i < expression.length(); i++) {
			t = tt;
			if (i != expression.length() - 1) {
				tt = expression.charAt(i + 1);
			} else {
				tt = 'E';
			}
			if (t >= '0' && t <= '9') {
				hasNum = true;
				if (tt == '(') {
					s.insert(i + add + 1, '*');
					add++;
				}
			} else if (t == '(') {
				sum++;
				if (tt == '.' || tt == '+' || tt == '-') {
					s.insert(i + add + 1, '0');
					add++;
				} 
				else if (tt == '*' || tt == '/' || tt == ')') {
					return false;
				}
			} else if (t == ')') {
				sum--;
				if (tt >= '0' && tt <= '9') {
					s.insert(i + add + 1, '*');
					add++;
				} 
				else if (tt == '.') {
					return false;
				}
			} else if (t == '+') {
				if (tt == '+' || tt == '-') {
					s.deleteCharAt(i + add);
					add--;
				} 
				else if (tt == '.') {
					s.insert(i + add + 1, '0');
					add++;
				} 
				else if (tt == '*' || tt == '/' || tt == ')') {
					return false;
				}
			} else if (t == '-') {
				if (tt == '+') {
					s.replace(i + add, i + add + 2, "-");
					add--;
				} 
				else if (tt == '-') {
					s.replace(i + add, i + add + 2, "+");
					add--;
				} 
				else if (tt == '.') {
					s.insert(i + add + 1, '0');
					add++;
				} 
				else if (tt == '*' || tt == '/' || tt == ')') {
					return false;
				}
			} else if (t == '*' || t == '/') {
				if (tt == ')' || tt == '+' || tt == '-' || tt == '*' || tt == '/') {
					return false;
				}
			} else if (t == '.') {
				if (tt == '.') {
					return false;
				} 
				else if (tt < '0' || tt > '9') {
					s.insert(i + add + 1, '0');
					add++;
				}
			} else {
				return false;
			}
		}
		if (sum == 0 && hasNum && (expression.indexOf('(') <= expression.indexOf(')'))) {
			expression = s.substring(0);
			return true;
		} else {
			return false;
		}
	}

	public static boolean isChinese(String con) {
		for (int i = 0; i < con.length(); i = i + 1) {
			if (Pattern.compile("[\u4e00-\u9fa5]").matcher(String.valueOf(con.charAt(i))).find()) {
				return true;
			}
		}
		return false;
	}

	public static boolean conValidate(String con) {
		if (null != con && !"".equals(con)) {
			if (GeneralUtil.isChinese(con) || con.matches(".*[a-zA-z].*")) {
				return true;
			}
		}
		return false;
	}

	public static XMLGregorianCalendar convertDate(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
		DatatypeFactory df = null;
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		XMLGregorianCalendar endDate = df.newXMLGregorianCalendar(new GregorianCalendar(year, month, day, hour, minute));
		return endDate;
	}

 

	public static <T> List<List<T>> averageAssign(List<T> source, int n) {
		List<List<T>> result = new ArrayList<List<T>>();
		int remaider = source.size() % n; // (先计算出余数)
		int number = source.size() / n; // 然后是商
		for (int i = 0; i < n; i++) {
			result.add( source.subList(i * number , (i + 1) * number));
		}
		if(remaider>0) {
			result.add(source.subList(n * number , n * number+remaider));
		}
		return result;
	}
	
	public static <T> List<List<T>> getListByPageSize(List<T> list, int pageSize) {
		List<List<T>> result = new ArrayList<List<T>>();
		int totalRows = list.size();
		int totalPages = totalRows / pageSize;
		if (totalRows % pageSize != 0) {
			totalPages = totalPages + 1;
		}
		for (int page = 1; page <= totalPages; page++) {
			List<T> tempskulist = getListWithLimit(list, page, pageSize);
			result.add(tempskulist);
		}
		return result;
	}
	
	/**
	 * 分页
	 * @param <T>
	 * @param list
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public static <T> List<T> getListWithLimit(List<T> list, int page, int pageSize) {
		List<T> tempskulist = null;
		int totalRows = list.size();
		int totalPages = totalRows / pageSize;
		if (totalRows % pageSize != 0) {
			totalPages = totalPages + 1;
		}
		int pageEndRow;
		int pageStartRow;
		if (totalRows > 0) {
			if (page * pageSize < totalRows) {// 判断是否为最后一页
				pageEndRow = page * pageSize;
				pageStartRow = pageEndRow - pageSize;
			} else {
				pageEndRow = totalRows;
				pageStartRow = pageSize * (totalPages - 1);
			}
			if (!list.isEmpty()) {
				tempskulist = list.subList(pageStartRow, pageEndRow);
			}
		}
		return tempskulist;
	}
	
	public final static Map<String, String> mapTimeZone = new HashMap<String, String>();  
	static {  
		mapTimeZone.put("UK", "Europe/London");
		mapTimeZone.put("FR", "Europe/Paris");  
		mapTimeZone.put("DE", "Europe/Paris");  
		mapTimeZone.put("ES", "Europe/Paris");  
		mapTimeZone.put("IT", "Europe/Paris"); 
		mapTimeZone.put("JP", "Asia/Tokyo");   
	 	mapTimeZone.put("IN", "Asia/Kolkata");//还没有加入广告
		mapTimeZone.put("AU", "Australia/Currie"); //还没有加入广告
		mapTimeZone.put("CA", "America/Los_Angeles"); 
		mapTimeZone.put("US", "America/Los_Angeles");
		
		mapTimeZone.put("MX", "America/Mexico_City");
		mapTimeZone.put("TR", "Europe/Ankara");
		mapTimeZone.put("BR", "America/Brasilia");
		mapTimeZone.put("NL", "Europe/Amsterdam");
		mapTimeZone.put("AE", "Asia/Dubai");
		mapTimeZone.put("SA", "Asia/Riyadh");
		mapTimeZone.put("PL", "Europe/Warsaw");
		mapTimeZone.put("SE", "Europe/Stockholm");
	}
	
	public static String getTimezone(String country,Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    format.setTimeZone(TimeZone.getTimeZone(mapTimeZone.get(country)));
		String startDay = format.format(time);
		return startDay;

	}
	
	public static Date getAfterHourDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 1);// 让日期加1
		return calendar.getTime();
	}
	
	//两个日期相差的天数
	public static int getDateDifferToDate(Date date) {
		long starttime=new Date().getTime();
		long endtime = date.getTime();
		long longvalue = (endtime-starttime)/(60*60*24*1000);
		return  Integer.parseInt(String.valueOf(longvalue));
	}
	
	public static boolean isConSpeCharacters(String string) {
		if (string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0) {
			// 如果不包含特殊字符
			return true;
		}
		return false;
	}

	public static boolean isSameObject(Object newobj, Object oldobj) {
		// TODO Auto-generated method stub
		Map<String, Object> newmap = null;
		Map<String, Object> oldmap =null;
		try {
			newmap =getFieldObjectMap(newobj);
		    oldmap = getFieldObjectMap(oldobj);
			for(Entry<String, Object> entry:newmap.entrySet()) {
				String key = entry.getKey();
				Object newvalue = entry.getValue();
				Object oldvalue = oldmap.get(key);
				if(newvalue!=null&&oldvalue!=null) {
					if(!newvalue.equals(oldvalue)) {
						return false;
					}
				}else if(newvalue!=null||oldvalue!=null) {
					return false;
				}
				
			}
			return true;
		}finally{
			if(newmap!=null) {
				newmap.clear();
				newmap=null;
			}
			if(oldmap!=null) {
				oldmap.clear();
				oldmap=null;
			}
		}
	}
	
	public static Map<String, String> xmlToMap(String strXML) throws Exception {
		try {
		        Map<String, String> data = new HashMap<String, String>();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		documentBuilderFactory.setXIncludeAware(false);
		documentBuilderFactory.setExpandEntityReferences(false);
		DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
		//DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
		InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
		org.w3c.dom.Document doc = documentBuilder.parse(stream);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		        for (int idx = 0; idx < nodeList.getLength(); ++idx) {
		            Node node = nodeList.item(idx);
		            if (node.getNodeType() == Node.ELEMENT_NODE) {
		                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
		data.put(element.getNodeName(), element.getTextContent());
		}
		        }
		try {
		            stream.close();
		} catch (Exception ex) {
		// do nothing
		}
		return data;
		} catch (Exception ex) {
		throw ex;
		}
		}
	
	 public static String encryptBASE64(byte[] data) {
		 Encoder encoder=Base64.getEncoder();
		 String encode=encoder.encodeToString(data);
		 return encode;
	 }
	 
	 public static byte[] decryptBASE64(String data) {
		 Decoder decoder=Base64.getDecoder();
		 byte[] buffer=decoder.decode(data);
		 return buffer;
	 }
	 
	 @SuppressWarnings("unchecked")
		public static List<Map<String, Object>> jsonStringToMapList(String list) {
			ArrayList<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
			if (isNotEmpty(list)) {
				String[] arr = list.split("},");
				for (int i = 0; i < arr.length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					if (i == arr.length - 1) {
						map = (Map<String, Object>) JSON.parse(arr[i]);
					} else {
						map = (Map<String, Object>) JSON.parse(arr[i] + "}");
					}
					maplist.add(map);
				}
				return maplist;
			}
			return null;
		}
	 
	 @SuppressWarnings("unchecked")
		public static List<Map<String, Object>> jsonStringToMultMapList(String list) {
			ArrayList<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
			if (isNotEmpty(list)) {
				String[] arr = list.split(",\\{\"");
				for (int i = 0; i < arr.length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					if (i == 0) {
						map = (Map<String, Object>) JSON.parse(arr[i]);
					} else {
						map = (Map<String, Object>) JSON.parse("{\"" + arr[i]);
					}
					maplist.add(map);
				}
				return maplist;
			}
			return null;
		}

	public static boolean isNotEmpty(String value) {
		// TODO Auto-generated method stub
		return !GeneralUtil.isEmpty(value);
	}


	public static BigDecimal getBigDecimal(String value) {
		if (StrUtil.isEmpty(value) || "N/A".equals(value)) {
			return null;
		}
		char[] array = value.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			char c = array[i];
			if (Character.isDigit(c) || '.' == c) {
				sb.append(c);
			}
		}
		value = sb.toString();
		if (value != null) {
			if (value.indexOf(".") == 0) {
				value = value.substring(1, value.length());
			}
			return new BigDecimal(value.toString().trim().indexOf(0) == '.' ? "0" + value : value);
		} else {
			return null;
		}
	}
	
	public static BigDecimal getIndexBigDecimal(String[] info, int i) {
		if (info.length > i) {
			String value = info[i];
			if (StrUtil.isEmpty(value)) {
				return null;
			} else {
				if (isDouble(value)) {
					return new BigDecimal(value);
				}
			}
		}
		return null;
	}
	
	public static Integer getInteger(String value) {
		if (StrUtil.isEmpty(value) || "N/A".equals(value)) {
			return null;
		}
		char[] array = value.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			char c = array[i];
			if (Character.isDigit(c)) {
				sb.append(c);
			} else if ('.' == c) {
				break;
			}
		}
		value = sb.toString();
		return   Integer.parseInt(value.toString().trim());
	}


	public static Date getIndexDate(String[] info, int i) {
		if (info.length > i) {
			String value = info[i];
			if (StrUtil.isEmpty(value)) {
				return null;
			} else {
				try {
					return GeneralUtil.getDatez(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static Long getIndexLong(String[] info, int i) {
		if (info.length > i) {
			String value = info[i];
			if (StrUtil.isEmpty(value)) {
				return null;
			} else {
				return Long.parseLong(value);
			}
		}
		return null;
	}

	public static BigDecimal getIndexEurBigDecimal(String[] info, int i) {
		if (info.length > i) {
			String value = info[i];
			value = value.replaceAll(",", ".");
			if (StrUtil.isEmpty(value)) {
				return null;
			} else {
				if (isDouble(value)) {
					return new BigDecimal(value);
				}
			}
		}
		return null;
	}
	
	public static String parseListForMapsToJsonArrayStr(List<Map<String, Object>> list) {
		String jsonArrayStr = null;
		if (list != null && list.size() > 0) {
			JSONArray jsonArray= new JSONArray();
			JSONObject jsonObject = null;
			Object value = null;
			for (Map<String, Object> map : list) {
				if (map != null) {
					jsonObject = new JSONObject();
					Set<String> set = map.keySet();
					for (String key : set) {
						value = map.get(key);
						if (value != null) {
							try {
								jsonObject.put(key, value.toString());
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
					if (jsonObject.size() != 0) {
						jsonArray.add(jsonObject);
					}
				}
			}
			jsonArrayStr = jsonArray.toString();
		}
		return jsonArrayStr;
	}
	public static double formatterNum(Object number) {// 保留2位小数，如：1000.00
		if (number == null || StrUtil.isEmpty(number.toString())) {
			return 0;
		}
		BigDecimal bnumber = new BigDecimal(number.toString());
		return bnumber.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public static Double formatterNum(Double rate, int i) {
		// TODO Auto-generated method stub
		if (rate == null || StrUtil.isEmpty(rate.toString())) {
			return 0.0;
		}
		BigDecimal bnumber = new BigDecimal(rate.toString());
		return bnumber.setScale(i, RoundingMode.HALF_UP).doubleValue();
	}

	public static String getValueWithoutBlank(String value) {
		// TODO Auto-generated method stub
		if(StrUtil.isBlankOrUndefined(value))return null;
		else return value.trim();
	}
   
	public static  Map<String,String> colorMap=null;
	
	public static Object getColorType(Object value) {
		   if(value==null)return null;
		   if(colorMap==null) {
				colorMap=new HashMap<String,String>();
		    	colorMap.put("red", "danger");
		    	colorMap.put("blue", "primary");
		    	colorMap.put("orange", "warning");
		    	colorMap.put("green", "success");
		    	colorMap.put("purple", "info");
		   }
	      String type=colorMap.get(value);
	      if(type!=null)return type;
	      else return value;
	    }

}
