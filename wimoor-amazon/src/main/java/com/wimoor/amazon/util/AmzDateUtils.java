package com.wimoor.amazon.util;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

 

public class AmzDateUtils {
	public static OffsetDateTime getOffsetDateTimeAll(Calendar c) {
		return OffsetDateTime.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, 
				c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),  c.get(Calendar.MILLISECOND), ZoneOffset.UTC);
	}
	
	public static OffsetDateTime getOffsetDateTime(Calendar c) {
		return OffsetDateTime.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), 0, 0, 0, 0, ZoneOffset.UTC);
	}
	
	public static Date getDate(OffsetDateTime date) {
		Calendar c=Calendar.getInstance();
	    c.set(date.getYear(), date.getMonth().getValue()-1, date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
	    return c.getTime();
	}

	public static LocalDateTime getLocalTime(String date) {
		// TODO Auto-generated method stub
		return LocalDateTime.of(0, 0, 0, 0, 0);
	}

	public static LocalDateTime getLocalTime(Date postdate) {
		// TODO Auto-generated method stub
		Calendar c=Calendar.getInstance();
		c.setTime(postdate);
		return LocalDateTime.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), 
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
	}
}
