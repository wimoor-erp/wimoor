package com.wimoor.amazon.adv.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
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
	public static void executThreadForAdv( List<Runnable> runnables, String  type) {
		ThreadPoolTaskExecutor threadPoolTaskExecutor=SpringUtil.getBean("threadPoolTaskExecutor");
		Integer runnumber=runMap.get(type);
		if(runnumber==null) {
			runnumber=0;
		}
		runMap.put(type, runnumber+1);
		if (runnables == null||(runnumber>1&&runnumber<5)) {
			return;
		}
			List<Future<?>> threadList = new ArrayList<Future<?>>();
			for (int i = 0; i < runnables.size(); i++) {
				if (runnables.get(i) != null) {
					threadList.add(threadPoolTaskExecutor.submit(runnables.get(i)));
				}
			}
		 new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
						int time = 1;
						boolean alldone = false;
						while (alldone == false && time < 10) {
							try {
								alldone = true;
								for (int i = 0; i < threadList.size(); i++) {
									if (!threadList.get(i).isDone()) {
										alldone = false;
										break;
									}
								}
								Thread.sleep(60000);
								time++;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < threadList.size(); i++) {
							if (!threadList.get(i).isDone()) {
								threadList.get(i).cancel(true);
							}
						}
						BlockingQueue<Runnable> que = threadPoolTaskExecutor.getThreadPoolExecutor().getQueue();
						if (que != null && que.size() > 0) {
							for (int i = 0; i < runnables.size(); i++) {
								Runnable runnable = runnables.get(i);
								if (!threadList.get(i).isDone()) {
									threadList.get(i).cancel(true);
									que.remove(runnable);
								}
							}
						}
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					runMap.put(type,0);
				}
			}
		}).start();
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
