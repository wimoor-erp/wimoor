package com.wimoor.admin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 把公历时间处理成农历时间
 *
 * class name: CalendarUtil
 * description:
 * authername:wangwenping
 * modified by: xx
 * modified time:
 * modified info :
 * @version 1.0.0
 *
 */
public class CalendarUtils {


    public static void main(String[] args) {
        Calendar ca = Calendar.getInstance();
        CalendarUtils c=new CalendarUtils(ca);
        System.out.println("农历："+c.getDay());


    }

    /**
     * 用于保存中文的月份
     */
    private final static String CHINESE_NUMBER[] = { "一", "二", "三", "四", "五",
            "六", "七", "八", "九", "十", "十一", "腊" };


    private final static long[] LUNAR_INFO = new long[] { 0x04bd8, 0x04ae0,
            0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0,
            0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540,
            0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5,
            0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
            0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3,
            0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0,
            0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0,
            0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8,
            0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570,
            0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5,
            0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
            0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50,
            0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0,
            0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
            0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7,
            0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50,
            0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954,
            0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260,
            0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0,
            0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0,
            0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20,
            0x0ada0 };

    /**
     * 转换为2012年11月22日格式
     */
    private static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
            "yyyy年MM月dd日");

    /**
     * 计算得到农历的年份
     */
    private int mLuchYear;
    /**
     * 计算得到农历的月份
     */
    private int mLuchMonth;

    /**
     * 计算得到农历的日期
     */
    private int mLuchDay;

    /**
     * 用于标识是事为闰年
     */
    private boolean isLoap;

    /**
     * 传回农历 year年的总天数
     *
     * @param year
     *            将要计算的年份
     * @return 返回传入年份的总天数
     */
    private static int yearDays(int year) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((LUNAR_INFO[year - 1900] & i) != 0)
                sum += 1;
        }
        return (sum + leapDays(year));
    }

    /**
     * 传回农历 year年闰月的天数
     *
     * @param year
     *            将要计算的年份
     * @return 返回 农历 year年闰月的天数
     */
    private static int leapDays(int year) {
        if (leapMonth(year) != 0) {
            if ((LUNAR_INFO[year - 1900] & 0x10000) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }

    /**
     * 传回农历 year年闰哪个月 1-12 , 没闰传回 0
     *
     * @param year
     *            将要计算的年份
     * @return 传回农历 year年闰哪个月 1-12 , 没闰传回 0
     */
    private static int leapMonth(int year) {
        return (int) (LUNAR_INFO[year - 1900] & 0xf);
    }

    /**
     * 传回农历 year年month月的总天数
     *
     * @param year
     *            将要计算的年份
     * @param month
     *            将要计算的月份
     * @return 传回农历 year年month月的总天数
     */
    private static int monthDays(int year, int month) {
        if ((LUNAR_INFO[year - 1900] & (0x10000 >> month)) == 0)
            return 29;
        else
            return 30;
    }

    /**
     * 传出y年m月d日对应的农历. yearCyl3:农历年与1864的相差数 ? monCyl4:从1900年1月31日以来,闰月数
     * dayCyl5:与1900年1月31日相差的天数,再加40 ?
     *
     * @param cal
     * @return
     */
    public CalendarUtils(Calendar cal) {
        int leapMonth = 0;
        Date baseDate = null;
        try {
            baseDate = chineseDateFormat.parse("1900年1月31日");
        } catch (ParseException e) {
            e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        // 求出和1900年1月31日相差的天数
        int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
        }
        // 农历年份
        mLuchYear = iYear;

        leapMonth = leapMonth(iYear); // 闰哪个月,1-12
        isLoap = false;

        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !isLoap) {
                --iMonth;
                isLoap = true;
                daysOfMonth = leapDays(mLuchYear);
            } else
                daysOfMonth = monthDays(mLuchYear, iMonth);

            offset -= daysOfMonth;
            // 解除闰月
            if (isLoap && iMonth == (leapMonth + 1))
                isLoap = false;
            if (!isLoap) {
            }
        }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (isLoap) {
                isLoap = false;
            } else {
                isLoap = true;
                --iMonth;
            }
        }
        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;

        }
        mLuchMonth = iMonth;
        mLuchDay = offset + 1;
    }

    /**
     * 返化成中文格式
     *
     * @param day
     * @return
     */
    public static String getChinaDayString(int day) {
        String chineseTen[] = { "初", "十", "廿", "卅" };
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30)
            return "";
        if (day == 10)
            return "初十";
        else
            return chineseTen[day / 10] + CHINESE_NUMBER[n];
    }

    /**
     * 返回农历的年月日
     *
     * @return 农历的年月日格式
     */
    public String getDay() {
        return (isLoap ? "闰" : "") + CHINESE_NUMBER[mLuchMonth - 1] + "月"
                + getChinaDayString(mLuchDay);
    }
    public String getSimpleDay() {
        return  getChinaDayString(mLuchDay);
    }

}




