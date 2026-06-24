package com.wimoor.finance.ledger.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.ledger.domain.FinDetailLedger;
import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;
import com.wimoor.finance.ledger.service.IFinDetailLedgerService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.util.QueryParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 明细账表Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/detail_ledger")
public class FinDetailLedgerController extends BaseController
{
    @Autowired
    private IFinDetailLedgerService finDetailLedgerService;
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;
    /**
     * 查询明细账表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinDetailLedger finDetailLedger)
    {
        startPage();
        List<FinDetailLedger> list = finDetailLedgerService.selectFinDetailLedgerList(finDetailLedger);
        return getDataTable(list);
    }

    /**
     * 查询明细账表列表
     */
    @GetMapping("/listInfo")
    public TableDataInfo listInfo(FinLedgerDTO finDetailLedger)
    {
        Date[] dateRange =  QueryParamUtil.parseDatePeriodRange(finDetailLedger.getStartPeriod(), finDetailLedger.getEndPeriod());
        finDetailLedger.setStartDate(dateRange[0]);
        finDetailLedger.setEndDate(dateRange[1]);
        List<Map<String,Object>> list = finDetailLedgerService.selectList(finDetailLedger);
        // 添加期初余额和月度汇总行
        list = addSummaryRows(list, finDetailLedger);
        TableDataInfo page = getDataTable(list);
        finAccountingSubjectsService.setFinLedgerSubject(page);
        return page;
    }

    /**
     * 查询明细账中使用的科目树（包含父科目）
     */
    @PostMapping("/treeSubjects")
    public Result  treeSubjects(@RequestBody FinLedgerDTO finDetailLedger)
    {
        Date[] dateRange =  QueryParamUtil.parseDatePeriodRange(finDetailLedger.getStartPeriod(), finDetailLedger.getEndPeriod());
        finDetailLedger.setStartDate(dateRange[0]);
        finDetailLedger.setEndDate(dateRange[1]);
        List<Map<String, Object>> list = finDetailLedgerService.selectSubjectTree(finDetailLedger);
        List<Map<String, Object>> tree = buildSubjectTree(list);
        return success(tree);
    }

    /**
     * 将平级科目列表构建为树形结构
     */
    private List<Map<String, Object>> buildSubjectTree(List<Map<String, Object>> list) {
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Map<String, Object>> codeMap = new HashMap<>();
        for (Map<String, Object> item : list) {
            String code = (String) item.get("subjectCode");
            codeMap.put(code, item);
        }
        for (Map<String, Object> item : list) {
            String parentCode = (String) item.get("parentCode");
            if (parentCode == null || parentCode.isEmpty() || !codeMap.containsKey(parentCode)) {
                tree.add(item);
            } else {
                Map<String, Object> parent = codeMap.get(parentCode);
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
                if (children == null) {
                    children = new ArrayList<>();
                    parent.put("children", children);
                }
                children.add(item);
            }
        }
        return tree;
    }

    /**
     * 添加期初余额和月度汇总行
     */
    private List<Map<String, Object>> addSummaryRows(List<Map<String, Object>> list, FinLedgerDTO finDetailLedger) {
        if (list == null || list.isEmpty()) {
            return list;
        }

        SimpleDateFormat monthFmt = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        // 获取查询的开始期间（格式：yyyyMM）
        String startPeriod = finDetailLedger.getStartPeriod();
        int startYear = Integer.parseInt(startPeriod.substring(0, 4));
        int startMonth = Integer.parseInt(startPeriod.substring(4, 6));

        // 获取期初余额（上个月最后一条记录的余额）
        BigDecimal openingBalance = BigDecimal.ZERO;
        Integer openingDirection = null;
        String subjectCode = finDetailLedger.getSubjectCode();
        String subjectId = finDetailLedger.getSubjectId();
        String groupid = finDetailLedger.getGroupid();

        // 从第一条记录获取groupid和subjectId（如果查询参数中没有）
        if (groupid == null || groupid.isEmpty()) {
            Object g = list.get(0).get("groupid");
            if (g != null) groupid = g.toString();
        }
        if (subjectId == null || subjectId.isEmpty()) {
            Object s = list.get(0).get("subjectId");
            if (s != null) subjectId = s.toString();
        }

        // 查询期初余额：取开始日期之前的最后一条记录的余额
        LocalDate startDate = LocalDate.of(startYear, startMonth, 1);
        if (subjectCode != null && !subjectCode.isEmpty()) {
            // 按subjectCode查询（父科目）：计算所有子科目的期初余额总和
            BigDecimal childBalance = finDetailLedgerService.sumChildSubjectsOpeningBalance(
                    groupid, subjectCode, startDate);
            openingBalance = childBalance != null ? childBalance : BigDecimal.ZERO;
            openingDirection = openingBalance.compareTo(BigDecimal.ZERO) >= 0 ? 1 : 2;
        } else if (subjectId != null && !subjectId.isEmpty()) {
            // 按subjectId查询（叶子科目）：查询该科目的期初余额
            String leafSubjectCode = null;
            for (Map<String, Object> record : list) {
                Object sc = record.get("subjectCode");
                if (sc != null && !sc.toString().isEmpty()) {
                    leafSubjectCode = sc.toString();
                    break;
                }
            }
            if (leafSubjectCode != null && !leafSubjectCode.isEmpty()) {
                List<FinDetailLedger> openingRecords = finDetailLedgerService.selectLatestBalanceBeforeDate(
                        groupid, leafSubjectCode, startDate);
                if (openingRecords != null && !openingRecords.isEmpty()) {
                    FinDetailLedger opening = openingRecords.get(0);
                    openingBalance = opening.getBalance() != null ? opening.getBalance() : BigDecimal.ZERO;
                    openingDirection = opening.getBalanceDirection();
                }
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        BigDecimal cumulativeDebit = BigDecimal.ZERO;
        BigDecimal cumulativeCredit = BigDecimal.ZERO;
        BigDecimal currentBalance = openingBalance;
        Integer currentDirection = openingDirection;

        // 按月分组处理
        String currentMonth = null;
        List<Map<String, Object>> monthRecords = new ArrayList<>();

        for (Map<String, Object> record : list) {
            Date voucherDate = (Date) record.get("voucherDate");
            if (voucherDate == null) {
                continue;
            }
            String month = monthFmt.format(voucherDate);

            // 如果是新月份，处理上一个月的汇总
            if (currentMonth != null && !month.equals(currentMonth)) {
                // 计算本期合计
                BigDecimal monthDebit = BigDecimal.ZERO;
                BigDecimal monthCredit = BigDecimal.ZERO;
                for (Map<String, Object> rec : monthRecords) {
                    BigDecimal debit = (BigDecimal) rec.get("debitAmount");
                    BigDecimal credit = (BigDecimal) rec.get("creditAmount");
                    monthDebit = monthDebit.add(debit != null ? debit : BigDecimal.ZERO);
                    monthCredit = monthCredit.add(credit != null ? credit : BigDecimal.ZERO);
                }

                // 更新累计
                cumulativeDebit = cumulativeDebit.add(monthDebit);
                cumulativeCredit = cumulativeCredit.add(monthCredit);

                // 计算本期余额
                currentBalance = currentBalance.add(monthDebit).subtract(monthCredit);
                if (currentBalance.compareTo(BigDecimal.ZERO) >= 0) {
                    currentDirection = 1; // 借
                } else {
                    currentDirection = 2; // 贷
                }

                // 添加本期合计行
                Map<String, Object> monthTotal = new HashMap<>();
                monthTotal.put("groupid", groupid);
                monthTotal.put("subjectId", subjectId);
                monthTotal.put("voucherDate", getMonthLastDay(currentMonth));
                monthTotal.put("summary", "本期合计");
                monthTotal.put("debitAmount", monthDebit);
                monthTotal.put("creditAmount", monthCredit);
                monthTotal.put("balance", currentBalance.abs());
                monthTotal.put("balanceDirection", currentDirection);
                result.add(monthTotal);

                // 添加本年累计行
                Map<String, Object> yearTotal = new HashMap<>();
                yearTotal.put("groupid", groupid);
                yearTotal.put("subjectId", subjectId);
                yearTotal.put("voucherDate", getMonthLastDay(currentMonth));
                yearTotal.put("summary", "本年累计");
                yearTotal.put("debitAmount", cumulativeDebit);
                yearTotal.put("creditAmount", cumulativeCredit);
                yearTotal.put("balance", currentBalance.abs());
                yearTotal.put("balanceDirection", currentDirection);
                result.add(yearTotal);

                monthRecords.clear();
            }

            // 如果是第一个月的第一条记录，添加期初余额
            if (currentMonth == null) {
                // 计算期初余额的借方和贷方
                BigDecimal openingDebit = BigDecimal.ZERO;
                BigDecimal openingCredit = BigDecimal.ZERO;
                if (openingBalance.compareTo(BigDecimal.ZERO) >= 0) {
                    openingDebit = openingBalance;
                } else {
                    openingCredit = openingBalance.abs();
                }

                Map<String, Object> openingRow = new HashMap<>();
                openingRow.put("groupid", groupid);
                openingRow.put("subjectId", subjectId);
                cal.set(startYear, startMonth - 1, 1);
                openingRow.put("voucherDate", cal.getTime());
                openingRow.put("summary", "期初余额");
                openingRow.put("debitAmount", BigDecimal.ZERO);
                openingRow.put("creditAmount", BigDecimal.ZERO);
                openingRow.put("balance", openingBalance.abs());
                openingRow.put("balanceDirection", openingDirection != null ? openingDirection : 1);
                result.add(openingRow);
            }

            currentMonth = month;
            monthRecords.add(record);
            result.add(record);
        }

        // 处理最后一个月的汇总
        if (!monthRecords.isEmpty()) {
            BigDecimal monthDebit = BigDecimal.ZERO;
            BigDecimal monthCredit = BigDecimal.ZERO;
            for (Map<String, Object> rec : monthRecords) {
                BigDecimal debit = (BigDecimal) rec.get("debitAmount");
                BigDecimal credit = (BigDecimal) rec.get("creditAmount");
                monthDebit = monthDebit.add(debit != null ? debit : BigDecimal.ZERO);
                monthCredit = monthCredit.add(credit != null ? credit : BigDecimal.ZERO);
            }

            cumulativeDebit = cumulativeDebit.add(monthDebit);
            cumulativeCredit = cumulativeCredit.add(monthCredit);

            currentBalance = currentBalance.add(monthDebit).subtract(monthCredit);
            if (currentBalance.compareTo(BigDecimal.ZERO) >= 0) {
                currentDirection = 1;
            } else {
                currentDirection = 2;
            }

            Map<String, Object> monthTotal = new HashMap<>();
            monthTotal.put("groupid", groupid);
            monthTotal.put("subjectId", subjectId);
            monthTotal.put("voucherDate", getMonthLastDay(currentMonth));
            monthTotal.put("summary", "本期合计");
            monthTotal.put("debitAmount", monthDebit);
            monthTotal.put("creditAmount", monthCredit);
            monthTotal.put("balance", currentBalance.abs());
            monthTotal.put("balanceDirection", currentDirection);
            result.add(monthTotal);

            Map<String, Object> yearTotal = new HashMap<>();
            yearTotal.put("groupid", groupid);
            yearTotal.put("subjectId", subjectId);
            yearTotal.put("voucherDate", getMonthLastDay(currentMonth));
            yearTotal.put("summary", "本年累计");
            yearTotal.put("debitAmount", cumulativeDebit);
            yearTotal.put("creditAmount", cumulativeCredit);
            yearTotal.put("balance", currentBalance.abs());
            yearTotal.put("balanceDirection", currentDirection);
            result.add(yearTotal);
        }

        return result;
    }

    /**
     * 获取指定月份的最后一天
     */
    private Date getMonthLastDay(String yearMonth) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM");
            Date date = fmt.parse(yearMonth);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            return cal.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 导出明细账表列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinDetailLedger finDetailLedger)
        {
        Date[] dateRange =  QueryParamUtil.parseDatePeriodRange(finDetailLedger.getStartPeriod(), finDetailLedger.getEndPeriod());
        finDetailLedger.setStartDate(dateRange[0]);
        finDetailLedger.setEndDate(dateRange[1]);
        List<FinDetailLedger> list = finDetailLedgerService.selectFinDetailLedgerList(finDetailLedger);
        ExcelUtil<FinDetailLedger> util = new ExcelUtil<FinDetailLedger>(FinDetailLedger.class);
        util.exportExcel(response, list, "明细账表数据");
    }

    /**
     * 获取明细账表详细信息
     */
    @GetMapping(value = "/{detailId}")
    public Result getInfo(@PathVariable("detailId") Long detailId)
    {
        return success(finDetailLedgerService.selectFinDetailLedgerByDetailId(detailId));
    }

    /**
     * 新增明细账表
     */
    @PostMapping
    public Result add(@RequestBody FinDetailLedger finDetailLedger)
    {
        return toResult(finDetailLedgerService.insertFinDetailLedger(finDetailLedger));
    }

    /**
     * 修改明细账表
     */
    @PutMapping
    public Result edit(@RequestBody FinDetailLedger finDetailLedger)
    {
        return toResult(finDetailLedgerService.updateFinDetailLedger(finDetailLedger));
    }

    /**
     * 删除明细账表
     */
	@DeleteMapping("/{detailIds}")
    public Result remove(@PathVariable Long[] detailIds)
    {
        return toResult(finDetailLedgerService.deleteFinDetailLedgerByDetailIds(detailIds));
    }
}