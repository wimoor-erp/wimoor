package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.CheckLevel;
import com.wimoor.finance.closing.domain.ClosingCheckItem;
import com.wimoor.finance.closing.domain.PreClosingCheckResult;
import com.wimoor.finance.closing.domain.VoucherSequenceInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessReasonableCheckService {

    /**
     * 检查业务数据合理性（而不是机械的连续性）
     */
    public void checkBusinessReasonableness(List<VoucherSequenceInfo> vouchers, String period, PreClosingCheckResult result) {

        if (vouchers.isEmpty()) {
            // 整个期间无凭证 - 这可能值得关注
            addNoVoucherCheckItem(result, period);
            return;
        }

        // 检查期间内凭证分布情况（作为信息，不是错误）
        checkVoucherDistribution(  period, vouchers, result);

        // 检查期末凭证集中情况（可能有意义）
        checkMonthEndVoucherConcentration(period, vouchers, result);
    }

    /**
     * 检查整个期间无凭证的情况
     */
    private void addNoVoucherCheckItem(PreClosingCheckResult result, String period) {
        ClosingCheckItem checkItem = new ClosingCheckItem();
        checkItem.setCheckItem("期间业务活动检查");
        checkItem.setPassed(true); // 不是错误，只是信息
        checkItem.setMessage("本期无任何凭证，请确认业务活动情况");
        checkItem.setCheckLevel(CheckLevel.INFO);
        checkItem.setSuggestion("如为新建账套或业务暂停，此情况正常");
        result.addCheckItem(checkItem);
    }

    /**
     * 检查凭证分布情况（信息性检查）
     */
    private void checkVoucherDistribution(String period,
                                          List<VoucherSequenceInfo> vouchers,
                                          PreClosingCheckResult result) {
        // 按日期统计凭证数量
        Map<Date, Long> voucherCountByDate = vouchers.stream()
                .collect(Collectors.groupingBy(VoucherSequenceInfo::getVoucherDate,
                        Collectors.counting()));

        long daysWithVouchers = voucherCountByDate.size();
        long totalDaysInPeriod = getTotalDaysInPeriod(period);

        // 计算有凭证的天数占比
        double coverageRate = (double) daysWithVouchers / totalDaysInPeriod * 100;

        ClosingCheckItem checkItem = new ClosingCheckItem();
        checkItem.setCheckItem("凭证日期分布");
        checkItem.setPassed(true); // 这只是一个信息性检查

        if (coverageRate < 30) {
            checkItem.setMessage(String.format("凭证日期分布较稀疏: %.1f%%的天数有凭证", coverageRate));
            checkItem.setCheckLevel(CheckLevel.INFO);
        } else if (coverageRate > 80) {
            checkItem.setMessage(String.format("凭证日期分布较密集: %.1f%%的天数有凭证", coverageRate));
            checkItem.setCheckLevel(CheckLevel.INFO);
        } else {
            checkItem.setMessage(String.format("凭证日期分布正常: %.1f%%的天数有凭证", coverageRate));
            checkItem.setCheckLevel(CheckLevel.INFO);
        }

        checkItem.addRelatedData("daysWithVouchers", daysWithVouchers);
        checkItem.addRelatedData("totalDays", totalDaysInPeriod);
        checkItem.addRelatedData("coverageRate", coverageRate);

        result.addCheckItem(checkItem);
    }

    /**
     * 检查月末凭证集中情况
     */
    private void checkMonthEndVoucherConcentration(String period,
                                                   List<VoucherSequenceInfo> vouchers,
                                                   PreClosingCheckResult result) {
        // 获取期间的最后5天
        List<Date> lastFiveDays = getLastFiveDaysOfPeriod(period);

        long vouchersInLastFiveDays = vouchers.stream()
                .filter(voucher -> isDateInList(voucher.getVoucherDate(), lastFiveDays))
                .count();

        double concentrationRate = (double) vouchersInLastFiveDays / vouchers.size() * 100;

        if (concentrationRate > 70) {
            // 超过70%的凭证集中在最后5天，这可能值得关注
            ClosingCheckItem checkItem = new ClosingCheckItem();
            checkItem.setCheckItem("期末凭证集中度");
            checkItem.setPassed(true); // 不是错误，只是警告
            checkItem.setMessage(String.format("%.1f%%的凭证集中在期末5天内", concentrationRate));
            checkItem.setCheckLevel(CheckLevel.WARNING);
            checkItem.setSuggestion("建议均衡凭证处理时间");
            checkItem.addRelatedData("concentrationRate", concentrationRate);
            result.addCheckItem(checkItem);
        }
    }

    /**
     * 获取期间总天数
     */
    private int getTotalDaysInPeriod(String period) {
        try {
            int year = Integer.parseInt(period.substring(0, 4));
            int month = Integer.parseInt(period.substring(4, 6));

            YearMonth yearMonth = YearMonth.of(year, month);
            return yearMonth.lengthOfMonth();
        } catch (Exception e) {
            return 30; // 默认值
        }
    }

    /**
     * 获取期间最后5天
     */
    private List<Date> getLastFiveDaysOfPeriod(String period) {
        List<Date> lastDays = new ArrayList<>();
        try {
            int year = Integer.parseInt(period.substring(0, 4));
            int month = Integer.parseInt(period.substring(4, 6));

            YearMonth yearMonth = YearMonth.of(year, month);
            int lastDay = yearMonth.lengthOfMonth();

            for (int day = lastDay - 4; day <= lastDay; day++) {
                if (day >= 1) {
                    LocalDate localDate = LocalDate.of(year, month, day);
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    lastDays.add(date);
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }
        return lastDays;
    }

    private boolean isDateInList(Date date, List<Date> dateList) {
        return dateList.stream()
                .anyMatch(d -> isSameDay(date, d));
    }

    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) return false;

        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return localDate1.equals(localDate2);
    }
}