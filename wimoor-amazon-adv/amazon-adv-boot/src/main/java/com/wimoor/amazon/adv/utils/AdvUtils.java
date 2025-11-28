package com.wimoor.amazon.adv.utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;

public class AdvUtils {
	public static String getRequestValue(HttpServletRequest request, String key) {
		String value = request.getParameter(key);
		if (StrUtil.isEmpty(value) || "all".equals(value)) {
			value = null;
		}
		return value;
	}
	
	public static String getRequestValue(String value) {
		if (StrUtil.isEmpty(value) || "all".equals(value)) {
			value = null;
		}
		return value;
	}
  
    
    public static String getKeyByTimeType(Map<String, Object> map,Calendar c){
    	String tempkey="";
    	Integer m= (c.get(Calendar.MONTH)+1);
    	String mstr=m<10?"0"+m.toString():m.toString();
    	Integer d=c.get(Calendar.DAY_OF_MONTH);
    	String dstr=d<10?"0"+d.toString():d.toString();
  	  if("Daily".equals(map.get("bytime"))){
		   tempkey =tempkey+  mstr;
		   tempkey =tempkey+ "-"+dstr ; 
		  }
  	  else if("Weekly".equals(map.get("bytime"))){
  		 tempkey= GeneralUtil.getSundayOfThisWeek(c.getTime());
  		 tempkey=tempkey.substring(tempkey.length()-5, tempkey.length());
  	  }
     else if("Monthly".equals(map.get("bytime"))){
			   tempkey =tempkey+  (c.get(Calendar.YEAR));
			   tempkey =tempkey+ "-"+ m; 
		  }
		return tempkey;
    }

    public static Boolean checkTimeType(Map<String, Object> map,Calendar c,Date beginDate,Date endDate){
    	 Calendar calendar = Calendar.getInstance();      
    	    if(endDate != null){        
    	         calendar.setTime(endDate);      
    	    }        
    	    //int w = calendar.get(Calendar.DAY_OF_WEEK)  ;  
    	    int m = calendar.get(Calendar.MONTH);
    	    int y = calendar.get(Calendar.YEAR);
      if(c.get(Calendar.YEAR)<y) return true;
  	  if("Daily".equals(map.get("bytime"))){
		     return c.getTime().equals(endDate)||c.getTime().before(endDate);
	  }
  	  else if("Weekly".equals(map.get("bytime"))){
  		    return c.getTime().equals(endDate)||  GeneralUtil.getSundayOfThisWeek(c.getTime()).compareTo(GeneralUtil.getSundayOfThisWeek(endDate))<=0;
  	  }
	  else if("Monthly".equals(map.get("bytime"))){
		    return  c.get(Calendar.YEAR)==y?c.get(Calendar.MONTH)<=m:c.get(Calendar.YEAR)<y; 
	  }
	return null;
	 
    }
    public static void step(Map<String, Object> map,Calendar c,Date beginDate,Date endDate){
  	  if("Daily".equals(map.get("bytime"))){
		    c.add(Calendar.DATE, 1);
		  }
	  else if("Weekly".equals(map.get("bytime"))){
		    c.add(Calendar.DATE,7); 
		  }
	   else if("Monthly".equals(map.get("bytime"))){
		   c.add(Calendar.MONTH,1); 
		  }
 
	 
    }
	static ConcurrentHashMap<String,Integer> runMap=new ConcurrentHashMap<String,Integer>();
	public static void executThreadForAdv( List<Runnable> runnables ) {
		executThreadForAdv( runnables, "normal",0,300);
	}

	public static void executThreadForAdv( List<Runnable> runnables, String  type,int maxTryNumber, int waitSecond) {
		if(runnables == null){return;}
		ThreadPoolTaskExecutor threadPoolTaskExecutor=SpringUtil.getBean("threadPoolTaskExecutor");
		Map<Future<?>, Long> startTimeMap = new ConcurrentHashMap<>();
		if(!type.equals("normal")){
			int tryNumber=runMap.get(type)!=null?runMap.get(type):0;
			runMap.put(type, tryNumber+1);
			if (tryNumber>1&&tryNumber<maxTryNumber) {
				return;
			}
		}
		List<Future<?>> futures = new ArrayList<Future<?>>();
		for (Runnable runnable : runnables) {
			 if(runnable!=null) {
				 Future<?> future = threadPoolTaskExecutor.submit(runnable);
				 futures.add(future);
				 startTimeMap.put(future, System.currentTimeMillis());
			 }
		}
		// 监控线程
		Thread monitorThread = new Thread(() -> {
			while (!futures.isEmpty()) {
				Iterator<Future<?>> iterator = futures.iterator();
				while (iterator.hasNext()) {
					Future<?> future = iterator.next();
					Long startTime = startTimeMap.get(future);
					if (startTime != null &&
							System.currentTimeMillis() - startTime > waitSecond* 1000L &&
							!future.isDone()) {

						System.out.println("检测到任务超时，正在取消...");
						boolean cancelled = future.cancel(true);
						if (cancelled) {
							System.out.println("超时任务取消成功");
							iterator.remove();
							startTimeMap.remove(future);
							if(startTimeMap.isEmpty() &&!type.equals("normal")){
								runMap.remove(type);
							}
						}
					} else if (future.isDone()) {
						// 任务正常完成
						iterator.remove();
						startTimeMap.remove(future);
						try {
							future.get(); // 检查是否有异常
							System.out.println("任务正常完成");
						} catch (CancellationException e) {
							System.out.println("任务被取消");
						} catch (Exception e) {
							System.out.println("任务执行异常: " + e.getMessage());
						}finally {
							if(startTimeMap.isEmpty() &&!type.equals("normal")){
								runMap.remove(type);
							}
						}
					}
				}

				try {
					Thread.sleep(1000); // 每秒检查一次
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});

		monitorThread.start();

	}


	public static boolean isNumeric(String str) {
		if (StrUtil.isEmpty(str)) {
			return false;
		}
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toString();
		} catch (Exception e) {
			return false;//异常 说明包含非数字。
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(bigStr);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
