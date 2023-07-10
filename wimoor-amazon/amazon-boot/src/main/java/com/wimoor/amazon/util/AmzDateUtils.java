package com.wimoor.amazon.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.wimoor.common.GeneralUtil;


 

public class AmzDateUtils {

	public static OffsetDateTime getOffsetDateTimeUTC(Calendar c) {
		return OffsetDateTime.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,
				c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR_OF_DAY), 
				c.get(Calendar.MINUTE), c.get(Calendar.SECOND),  
				c.get(Calendar.MILLISECOND), ZoneOffset.ofHours(8));
	}
	public static OffsetDateTime getOffsetDateTimeUTC(Date date) {
		 Calendar c=Calendar.getInstance();
		 c.setTime(date);
		return getOffsetDateTimeUTC(c);
	}
	
	public static Date getUTCToDate(OffsetDateTime date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    try {
			DateTimeFormatter df=DateTimeFormatter.ISO_INSTANT;
			return sdf.parse(date.format(df));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
	
	public static Date getDate(OffsetDateTime date) {
		ZoneOffset OFFSET_ID = ZoneOffset.of("+08:00");
		Calendar c=Calendar.getInstance();
		ZonedDateTime zonetime = date.atZoneSameInstant(OFFSET_ID);
	    c.set(zonetime.getYear(), zonetime.getMonth().getValue()-1, zonetime.getDayOfMonth(), zonetime.getHour(), zonetime.getMinute(), zonetime.getSecond());
	    return c.getTime();
	}
 
	
	public static String  getDateToUTCStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    return sdf.format(date);
	}


	public static LocalDateTime getLocalTime(String date) {
		// TODO Auto-generated method stub
		if(date==null)return null;
		date=date.replace("/", "-");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date time = null;
		try {
			  time = sdf.parse(date);
		} catch (ParseException e) {
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
			try {
				time = sdf2.parse(date);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(time!=null) {
			return getLocalTime(time);
		}else {
		   return	null;
		}
	}

	public static LocalDateTime getLocalTime(Date postdate) {
		// TODO Auto-generated method stub
		Calendar c=Calendar.getInstance();
		c.setTime(postdate);
		return LocalDateTime.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), 
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
	}
	
	public static LocalDate getLocalDate(Date postdate) {
		// TODO Auto-generated method stub
		if(postdate==null)return null;
		Calendar c=Calendar.getInstance();
		c.setTime(postdate);
		return LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
	}
	
	public static Date getDate(LocalDateTime date) {
		if(date==null) {
			return null;
		}
	    Calendar c=Calendar.getInstance();
	    c.set(date.getYear(), date.getMonth().getValue()-1, date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
	    return c.getTime();
	}
	
	public static Date getDate(Object o) {
		if(o==null) {
			   return null;
		}else if(o instanceof Date) {
			    return (Date)o;
		}else if(o instanceof Long) {
			    return new Date((Long)o);
		}else if(o instanceof LocalDateTime) {
				LocalDateTime date=(LocalDateTime)o;
				return getDate(date);
		}else if(o instanceof OffsetDateTime){
			   OffsetDateTime date=(OffsetDateTime)o;
			   return getDate(date);
		}else {
			SimpleDateFormat fmt =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
}
