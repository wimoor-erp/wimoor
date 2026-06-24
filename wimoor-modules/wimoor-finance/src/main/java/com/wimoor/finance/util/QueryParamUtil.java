package com.wimoor.finance.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class QueryParamUtil {

    /**
     * 解析期间范围（支持多期间查询）
     *
     * @param beginPeriod 开始期间（YYYYMM格式）
     * @param endPeriod 结束期间（YYYYMM格式）
     * @return 日期范围数组 [开始日期, 结束日期]
     */
    public static LocalDate[] parsePeriodRange(String beginPeriod, String endPeriod) {
        // 优先使用 beginPeriod 和 endPeriod（多期间查询）
        if (beginPeriod != null && !beginPeriod.isEmpty() && endPeriod != null && !endPeriod.isEmpty()) {
            if (beginPeriod.length() != 6) {
                throw new IllegalArgumentException("开始期间格式错误，应为YYYYMM: " + beginPeriod);
            }
            if (endPeriod.length() != 6) {
                throw new IllegalArgumentException("结束期间格式错误，应为YYYYMM: " + endPeriod);
            }

            int beginYear = Integer.parseInt(beginPeriod.substring(0, 4));
            int beginMonth = Integer.parseInt(beginPeriod.substring(4, 6));
            int endYear = Integer.parseInt(endPeriod.substring(0, 4));
            int endMonth = Integer.parseInt(endPeriod.substring(4, 6));

            LocalDate startDate = LocalDate.of(beginYear, beginMonth, 1);
            LocalDate endDate = LocalDate.of(endYear, endMonth, 1).withDayOfMonth(
                    LocalDate.of(endYear, endMonth, 1).lengthOfMonth());

            return new LocalDate[]{startDate, endDate};
        }

        // 使用默认期间（当前月份）
        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return new LocalDate[]{startDate, endDate};
    }

    public static Date[] parseDatePeriodRange(String beginPeriod, String endPeriod) {
        // 优先使用 beginPeriod 和 endPeriod（多期间查询）
        if (beginPeriod != null && !beginPeriod.isEmpty() && endPeriod != null && !endPeriod.isEmpty()) {
            if (beginPeriod.length() != 6) {
                throw new IllegalArgumentException("开始期间格式错误，应为YYYYMM: " + beginPeriod);
            }
            if (endPeriod.length() != 6) {
                throw new IllegalArgumentException("结束期间格式错误，应为YYYYMM: " + endPeriod);
            }

            int beginYear = Integer.parseInt(beginPeriod.substring(0, 4));
            int beginMonth = Integer.parseInt(beginPeriod.substring(4, 6));
            int endYear = Integer.parseInt(endPeriod.substring(0, 4));
            int endMonth = Integer.parseInt(endPeriod.substring(4, 6));

            LocalDate startLocalDate = LocalDate.of(beginYear, beginMonth, 1);
            LocalDate endLocalDate = LocalDate.of(endYear, endMonth, 1)
                    .withDayOfMonth(LocalDate.of(endYear, endMonth, 1).lengthOfMonth());

            // 转换为 java.util.Date，开始日期为当天 00:00:00，结束日期为当天 23:59:59.999
            Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endLocalDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

            return new Date[]{startDate, endDate};
        }

        // 使用默认期间（当前月份）
        LocalDate now = LocalDate.now();
        LocalDate startLocalDate = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
        LocalDate endLocalDate = startLocalDate.withDayOfMonth(startLocalDate.lengthOfMonth());

        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endLocalDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        return new Date[]{startDate, endDate};
    }
}
