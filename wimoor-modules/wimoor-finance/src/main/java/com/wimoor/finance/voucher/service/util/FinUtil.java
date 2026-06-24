package com.wimoor.finance.voucher.service.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class FinUtil {
    /**
     * 获取会计期间 (YYYYMM格式)
     */
    public static String getAccountingPeriod(Date voucherDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(voucherDate);
    }

    public static String getAccountingPeriod(LocalDate voucherDate) {
        Date date = Date.from(voucherDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
        return getAccountingPeriod(date);
    }

    public static Date getFirstDayOfMonth(int year, int month) {
        //获取year ,month对应的月份的第一天日期
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);  // Calendar月份从0开始
        calendar.set(Calendar.DAY_OF_MONTH, 1);   // 设置为该月的第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);    // 将小时设为0
        calendar.set(Calendar.MINUTE, 0);         // 将分钟设为0
        calendar.set(Calendar.SECOND, 0);         // 将秒设为0
        calendar.set(Calendar.MILLISECOND, 0);    // 将毫秒设为0
        return calendar.getTime();


    }

    public static Date getLastDayOfMonth(int year, int month) {
        //获取year ,month对应的月份的最后一天日期
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
