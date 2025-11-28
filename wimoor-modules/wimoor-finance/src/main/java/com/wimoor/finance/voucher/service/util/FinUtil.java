package com.wimoor.finance.voucher.service.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
}
