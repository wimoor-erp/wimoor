package com.wimoor.finance.ledger.service.impl;

import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;
import com.wimoor.finance.ledger.mapper.FinGeneralLedgerMapper;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 总账Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinGeneralLedgerServiceImpl implements IFinGeneralLedgerService 
{
    @Autowired
    private FinGeneralLedgerMapper finGeneralLedgerMapper;

    /**
     * 查询总账
     * 
     * @param ledgerId 总账主键
     * @return 总账
     */
    @Override
    public FinGeneralLedger selectFinGeneralLedgerByLedgerId(Long ledgerId)
    {
        return finGeneralLedgerMapper.selectFinGeneralLedgerByLedgerId(ledgerId);
    }

    /**
     * 查询总账列表
     * 
     * @param finGeneralLedger 总账
     * @return 总账
     */
    @Override
    public List<FinGeneralLedger> selectFinGeneralLedgerList(FinGeneralLedger finGeneralLedger)
    {
        return finGeneralLedgerMapper.selectFinGeneralLedgerList(finGeneralLedger);
    }

    /**
     * 查询总账列表
     *
     * @param finGeneralLedger 总账
     * @return 总账
     */
    @Override
    public List<Map<String,Object>> selectList(FinLedgerDTO finGeneralLedger)
    {
        return finGeneralLedgerMapper.selectList(finGeneralLedger);
    }

    /**
     * 新增总账
     * 
     * @param finGeneralLedger 总账
     * @return 结果
     */
    @Override
    public int insertFinGeneralLedger(FinGeneralLedger finGeneralLedger)
    {
        return finGeneralLedgerMapper.insertFinGeneralLedger(finGeneralLedger);
    }

    /**
     * 修改总账
     * 
     * @param finGeneralLedger 总账
     * @return 结果
     */
    @Override
    public int updateFinGeneralLedger(FinGeneralLedger finGeneralLedger)
    {
        return finGeneralLedgerMapper.updateFinGeneralLedger(finGeneralLedger);
    }

    /**
     * 批量删除总账
     * 
     * @param ledgerIds 需要删除的总账主键
     * @return 结果
     */
    @Override
    public int deleteFinGeneralLedgerByLedgerIds(Long[] ledgerIds)
    {
        return finGeneralLedgerMapper.deleteFinGeneralLedgerByLedgerIds(ledgerIds);
    }

    /**
     * 删除总账信息
     * 
     * @param ledgerId 总账主键
     * @return 结果
     */
    @Override
    public int deleteFinGeneralLedgerByLedgerId(Long ledgerId)
    {
        return finGeneralLedgerMapper.deleteFinGeneralLedgerByLedgerId(ledgerId);
    }

    @Override
    public FinGeneralLedger selectBySubjectAndPeriod(String groupid, String subjectId, String period) {
        return this.finGeneralLedgerMapper.selectBySubjectAndPeriod(groupid, subjectId, period);
    }

    @Override
    public FinGeneralLedger selectBySubjectCodeAndPeriod(String groupid, String subjectCodes, String period) {
        return this.finGeneralLedgerMapper.selectBySubjectCodeAndPeriod(groupid, subjectCodes, period);
    }

    @Override
    public int deleteByPeriod(String groupid, String nextPeriod) {
        return this.finGeneralLedgerMapper.deleteByPeriod(groupid, nextPeriod);
    }

    @Override
    public List<Map<String, Object>> getAllSubjectBalance(String groupid, String period) {
        return this.finGeneralLedgerMapper.getAllSubjectBalance(groupid, period);
    }

    @Override
    public List<Map<String, Object>> selectTopLevelSummary(FinLedgerDTO dto) {
        return this.finGeneralLedgerMapper.selectTopLevelSummary(dto);
    }

    @Override
    public List<Map<String, Object>> selectTopLevelSummaryFull(FinLedgerDTO dto) {
        // 1. 查询基础数据
        List<Map<String, Object>> rawData = this.finGeneralLedgerMapper.selectTopLevelSummary(dto);
        
        // 2. 按科目分组
        Map<String, List<Map<String, Object>>> subjectMap = new LinkedHashMap<>();
        if (rawData != null && !rawData.isEmpty()) {
            for (Map<String, Object> item : rawData) {
                String subjectCode = item.get("subjectCode").toString();
                subjectMap.computeIfAbsent(subjectCode, k -> new ArrayList<>()).add(item);
            }
        }
        
        // 3. 查询从年初到当前期间有余额的科目，补充缺失的科目
        String yearStart = dto.getStartPeriod().substring(0, 4) + "01";
        List<Map<String, Object>> yearData = this.finGeneralLedgerMapper.selectTopLevelSummaryByDateRange(
            dto.getGroupid(), yearStart, dto.getStartPeriod());
        if (yearData != null) {
            for (Map<String, Object> item : yearData) {
                String subjectCode = item.get("subjectCode").toString();
                // 如果该科目在当前查询结果中不存在，则添加
                if (!subjectMap.containsKey(subjectCode)) {
                    // 检查是否有余额
                    BigDecimal endBalance = item.get("endBalance") != null ? 
                        new BigDecimal(item.get("endBalance").toString()) : BigDecimal.ZERO;
                    Integer endDirection = item.get("endDirection") != null ? 
                        Integer.parseInt(item.get("endDirection").toString()) : 1;
                    
                    if (endBalance.compareTo(BigDecimal.ZERO) != 0) {
                        subjectMap.computeIfAbsent(subjectCode, k -> new ArrayList<>()).add(item);
                    }
                }
            }
        }
        
        if (subjectMap.isEmpty()) {
            return new ArrayList<>();
        }

        // 4. 查询年初到查询起始期间之前期间的数据，用于预累加本年累计借方/贷方
        String startYear = dto.getStartPeriod().substring(0, 4);
        String yearStartPeriod = startYear + "01";
        String prevStartPeriod = getPrevPeriod(dto.getStartPeriod());
        Map<String, BigDecimal[]> yearPreDebitCreditMap = new LinkedHashMap<>();
        if (yearStartPeriod.compareTo(prevStartPeriod) <= 0) {
            List<Map<String, Object>> yearPreData = this.finGeneralLedgerMapper.selectTopLevelSummaryByDateRange(
                dto.getGroupid(), yearStartPeriod, prevStartPeriod);
            if (yearPreData != null) {
                for (Map<String, Object> item : yearPreData) {
                    String sc = item.get("subjectCode").toString();
                    BigDecimal debit = item.get("debitTotal") != null ? new BigDecimal(item.get("debitTotal").toString()) : BigDecimal.ZERO;
                    BigDecimal credit = item.get("creditTotal") != null ? new BigDecimal(item.get("creditTotal").toString()) : BigDecimal.ZERO;
                    BigDecimal[] existing = yearPreDebitCreditMap.get(sc);
                    if (existing != null) {
                        existing[0] = existing[0].add(debit);
                        existing[1] = existing[1].add(credit);
                    } else {
                        yearPreDebitCreditMap.put(sc, new BigDecimal[]{debit, credit});
                    }
                }
            }
        }

        // 5. 生成完整的期间列表
        List<String> allPeriods = generatePeriodRange(dto.getStartPeriod(), dto.getEndPeriod());

        // 6. 构建完整的总账数据
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Map.Entry<String, List<Map<String, Object>>> entry : subjectMap.entrySet()) {
            String subjectCode = entry.getKey();
            List<Map<String, Object>> periodList = entry.getValue();
            
            // 按期间排序
            periodList.sort((a, b) -> a.get("period").toString().compareTo(b.get("period").toString()));
            
            // 获取科目信息
            String subjectName = periodList.get(0).get("subjectName") != null ? 
                periodList.get(0).get("subjectName").toString() : "";
            
            // 将凭证数据按期间建立索引
            Map<String, Map<String, Object>> periodDataMap = new LinkedHashMap<>();
            for (Map<String, Object> periodData : periodList) {
                periodDataMap.put(periodData.get("period").toString(), periodData);
            }
            
            // 本年累计变量（每年重新计算）
            BigDecimal yearCumulativeDebit = BigDecimal.ZERO;
            BigDecimal yearCumulativeCredit = BigDecimal.ZERO;
            String currentYear = "";
            
            // 上期期末余额（用于填补缺失期间）
            BigDecimal lastEndBalance = BigDecimal.ZERO;
            Integer lastEndDirection = 1;
            
            // 初始化：从periodDataMap中找到查询期间之前的最新记录，获取其期末余额
            if (!periodDataMap.isEmpty()) {
                // 找到查询期间之前的最新期间数据
                String targetPeriod = allPeriods.isEmpty() ? null : allPeriods.get(0);
                if (targetPeriod != null) {
                    // 找到targetPeriod之前（不包括targetPeriod）的最新期间
                    String prevLatestPeriod = periodDataMap.keySet().stream()
                        .filter(p -> p.compareTo(targetPeriod) < 0)
                        .max(String::compareTo)
                        .orElse(null);
                    if (prevLatestPeriod != null) {
                        Map<String, Object> prevData = periodDataMap.get(prevLatestPeriod);
                        lastEndBalance = prevData.get("endBalance") != null ? 
                            new BigDecimal(prevData.get("endBalance").toString()) : BigDecimal.ZERO;
                        lastEndDirection = prevData.get("endDirection") != null ? 
                            Integer.parseInt(prevData.get("endDirection").toString()) : 1;
                    }
                }
            }
            
            // 遍历所有期间
            boolean isFirstPeriod = true;
            for (String period : allPeriods) {
                String periodYear = period.substring(0, 4);
                
                // 如果是新的一年，重置本年累计（并预累加年初到查询起始期间之前的借方/贷方）
                if (!periodYear.equals(currentYear)) {
                    currentYear = periodYear;
                    if (currentYear.equals(startYear)) {
                        BigDecimal[] preDC = yearPreDebitCreditMap.get(subjectCode);
                        yearCumulativeDebit = preDC != null ? preDC[0] : BigDecimal.ZERO;
                        yearCumulativeCredit = preDC != null ? preDC[1] : BigDecimal.ZERO;
                    } else {
                        yearCumulativeDebit = BigDecimal.ZERO;
                        yearCumulativeCredit = BigDecimal.ZERO;
                    }
                }
                
                // 获取该期间的数据，如果没有则使用上期期末余额
                Map<String, Object> periodData = periodDataMap.get(period);
                
                BigDecimal beginBalance;
                Integer beginDirection;
                BigDecimal periodDebit;
                BigDecimal periodCredit;
                BigDecimal periodEndBalance;
                Integer periodDirection;
                
                if (periodData != null) {
                    // 有数据，使用表中的借贷发生额
                    // 期初余额使用上期结转的期末余额
                    beginBalance = lastEndBalance;
                    beginDirection = lastEndDirection;
                    periodDebit = periodData.get("debitTotal") != null ? 
                        new BigDecimal(periodData.get("debitTotal").toString()) : BigDecimal.ZERO;
                    periodCredit = periodData.get("creditTotal") != null ? 
                        new BigDecimal(periodData.get("creditTotal").toString()) : BigDecimal.ZERO;
                    
                    // 增量计算期末余额：上期期末余额 + 本期借方 - 本期贷方
                    // lastEndBalance 是带符号的值（正=借，负=贷）
                    BigDecimal signedNewBalance = lastEndBalance.add(periodDebit).subtract(periodCredit);
                    if (signedNewBalance.compareTo(BigDecimal.ZERO) > 0) {
                        periodDirection = 1;
                        periodEndBalance = signedNewBalance;
                    } else if (signedNewBalance.compareTo(BigDecimal.ZERO) < 0) {
                        periodDirection = 2;
                        periodEndBalance = signedNewBalance;
                    } else {
                        periodDirection = 0;
                        periodEndBalance = BigDecimal.ZERO;
                    }
                    
                    // 更新上期期末余额
                    lastEndBalance = periodEndBalance;
                    lastEndDirection = periodDirection;
                } else {
                    // 没有数据，使用上期期末余额作为期初和期末
                    beginBalance = lastEndBalance;
                    beginDirection = lastEndDirection;
                    periodDebit = BigDecimal.ZERO;
                    periodCredit = BigDecimal.ZERO;
                    periodEndBalance = lastEndBalance;
                    periodDirection = lastEndDirection;
                }
                
                // 如果期初余额为0，方向为平
                if (beginBalance.compareTo(BigDecimal.ZERO) == 0) {
                    beginDirection = 0;
                }
                
                // 根据方向处理余额：确保余额显示为正数
                BigDecimal displayBeginBalance = beginBalance;
                if (beginDirection == 2) {
                    // 贷方余额，显示为正数
                    displayBeginBalance = beginBalance.abs();
                }
                
                // 添加期初余额行（只在第一个期间显示）
                if (isFirstPeriod) {
                    Map<String, Object> beginRow = new LinkedHashMap<>();
                    beginRow.put("subjectCode", subjectCode);
                    beginRow.put("subjectName", subjectName);
                    beginRow.put("period", period);
                    beginRow.put("summary", "期初余额");
                    beginRow.put("debitTotal", null);
                    beginRow.put("creditTotal", null);
                    beginRow.put("direction", beginDirection);
                    beginRow.put("balance", displayBeginBalance);
                    beginRow.put("rowType", "begin");
                    result.add(beginRow);
                    isFirstPeriod = false;
                }
                
                // 累加本年累计
                yearCumulativeDebit = yearCumulativeDebit.add(periodDebit);
                yearCumulativeCredit = yearCumulativeCredit.add(periodCredit);
                
                // 如果期末余额为0，方向为平
                if (periodEndBalance.compareTo(BigDecimal.ZERO) == 0) {
                    periodDirection = 0;
                }
                
                // 根据方向处理余额：确保余额显示为正数
                BigDecimal displayEndBalance = periodEndBalance;
                if (periodDirection == 2) {
                    // 贷方余额，显示为正数
                    displayEndBalance = periodEndBalance.abs();
                }
                
                // 本期合计行
                Map<String, Object> periodRow = new LinkedHashMap<>();
                periodRow.put("subjectCode", subjectCode);
                periodRow.put("subjectName", subjectName);
                periodRow.put("period", period);
                periodRow.put("summary", "本期合计");
                periodRow.put("debitTotal", periodDebit);
                periodRow.put("creditTotal", periodCredit);
                periodRow.put("direction", periodDirection);
                periodRow.put("balance", displayEndBalance);
                periodRow.put("rowType", "period");
                result.add(periodRow);
                
                // 本年累计行
                // 本年累计余额 = 当期期末余额（从年初到当前期间的累计余额）
                Map<String, Object> yearRow = new LinkedHashMap<>();
                yearRow.put("subjectCode", subjectCode);
                yearRow.put("subjectName", subjectName);
                yearRow.put("period", period);
                yearRow.put("summary", "本年累计");
                yearRow.put("debitTotal", yearCumulativeDebit);
                yearRow.put("creditTotal", yearCumulativeCredit);
                yearRow.put("direction", periodDirection);
                yearRow.put("balance", displayEndBalance);
                yearRow.put("rowType", "year");
                result.add(yearRow);
            }
        }
        
        return result;
    }

    @Override
    public int rebuildGeneralLedger(String groupid) {
        // 1. 删除该租户所有总账记录
        finGeneralLedgerMapper.deleteAllByGroupid(groupid);

        // 2. 从凭证分录汇总各科目各期间的借贷发生额
        List<Map<String, Object>> entries = finGeneralLedgerMapper.sumEntriesBySubjectAndPeriod(groupid);
        if (entries == null || entries.isEmpty()) {
            return 0;
        }

        // 3. 按科目分组，每个科目的期间按顺序排列
        Map<String, List<Map<String, Object>>> subjectMap = new LinkedHashMap<>();
        for (Map<String, Object> entry : entries) {
            String subjectId = entry.get("subjectId").toString();
            subjectMap.computeIfAbsent(subjectId, k -> new ArrayList<>()).add(entry);
        }

        // 4. 逐科目、逐期间重建总账
        int count = 0;
        for (Map.Entry<String, List<Map<String, Object>>> subjectEntry : subjectMap.entrySet()) {
            String subjectId = subjectEntry.getKey();
            List<Map<String, Object>> periodList = subjectEntry.getValue();

            // 期间已按 SQL ORDER BY 排序
            BigDecimal signedBalance = BigDecimal.ZERO; // 带符号的余额：借方为正，贷方为负

            for (Map<String, Object> periodData : periodList) {
                String period = periodData.get("period").toString();
                BigDecimal debitTotal = new BigDecimal(periodData.get("debitTotal").toString());
                BigDecimal creditTotal = new BigDecimal(periodData.get("creditTotal").toString());

                // 计算期末余额（带符号）
                signedBalance = signedBalance.add(debitTotal).subtract(creditTotal);

                // 确定方向和带符号的余额
                Integer endDirection;
                BigDecimal endBalance;
                if (signedBalance.compareTo(BigDecimal.ZERO) > 0) {
                    endDirection = 1; // 借
                    endBalance = signedBalance;
                } else if (signedBalance.compareTo(BigDecimal.ZERO) < 0) {
                    endDirection = 2; // 贷
                    endBalance = signedBalance; // 保持负数，便于汇总计算
                } else {
                    endDirection = 0; // 平
                    endBalance = BigDecimal.ZERO;
                }

                // 构建总账记录
                FinGeneralLedger ledger = new FinGeneralLedger();
                ledger.setGroupid(groupid);
                ledger.setSubjectId(Long.parseLong(subjectId));
                ledger.setPeriod(period);
                ledger.setBeginBalance(BigDecimal.ZERO); // 会被下面覆盖
                ledger.setBeginDirection(1);
                ledger.setDebitTotal(debitTotal);
                ledger.setCreditTotal(creditTotal);
                ledger.setEndBalance(endBalance);
                ledger.setEndDirection(endDirection);
                ledger.setCreatedTime(new Date());
                ledger.setUpdatedTime(new Date());

                finGeneralLedgerMapper.insertFinGeneralLedger(ledger);
                count++;
            }
        }

        // 5. 回填各期间的 begin_balance（从上期的 end_balance 获取）
        fillBeginBalances(groupid);

        return count;
    }

    /**
     * 获取上一个期间
     */
    private String getPrevPeriod(String period) {
        if (period == null || period.length() != 6) {
            return null;
        }
        int year = Integer.parseInt(period.substring(0, 4));
        int month = Integer.parseInt(period.substring(4, 6));
        month--;
        if (month < 1) {
            month = 12;
            year--;
        }
        return String.format("%d%02d", year, month);
    }

    /**
     * 生成期间范围列表
     */
    private List<String> generatePeriodRange(String startPeriod, String endPeriod) {
        List<String> periods = new ArrayList<>();
        if (startPeriod == null || endPeriod == null) {
            return periods;
        }

        int startYear = Integer.parseInt(startPeriod.substring(0, 4));
        int startMonth = Integer.parseInt(startPeriod.substring(4, 6));
        int endYear = Integer.parseInt(endPeriod.substring(0, 4));
        int endMonth = Integer.parseInt(endPeriod.substring(4, 6));

        int currentYear = startYear;
        int currentMonth = startMonth;

        while (currentYear < endYear || (currentYear == endYear && currentMonth <= endMonth)) {
            periods.add(String.format("%d%02d", currentYear, currentMonth));
            currentMonth++;
            if (currentMonth > 12) {
                currentMonth = 1;
                currentYear++;
            }
        }

        return periods;
    }

    /**
     * 回填各期间的期初余额
     */
    private void fillBeginBalances(String groupid) {
        // 查询所有刚插入的总账记录，按科目和期间排序
        FinGeneralLedger query = new FinGeneralLedger();
        query.setGroupid(groupid);
        List<FinGeneralLedger> allLedgers = finGeneralLedgerMapper.selectFinGeneralLedgerList(query);

        // 按科目分组
        Map<Long, List<FinGeneralLedger>> subjectMap = new LinkedHashMap<>();
        for (FinGeneralLedger ledger : allLedgers) {
            subjectMap.computeIfAbsent(ledger.getSubjectId(), k -> new ArrayList<>()).add(ledger);
        }

        // 逐科目回填
        for (List<FinGeneralLedger> ledgerList : subjectMap.values()) {
            // 按期间排序
            ledgerList.sort(Comparator.comparing(FinGeneralLedger::getPeriod));

            for (int i = 0; i < ledgerList.size(); i++) {
                FinGeneralLedger current = ledgerList.get(i);
                if (i == 0) {
                    // 第一个期间，需要查询上一个期间的期末余额作为期初
                    String firstPeriod = current.getPeriod(); // 例如 "202601"
                    String prevPeriod = getPrevPeriod(firstPeriod); // 计算 "202512"
                    
                    // 查询上期期末余额
                    FinGeneralLedger prevLedger = finGeneralLedgerMapper.selectBySubjectAndPeriod(
                        groupid, String.valueOf(current.getSubjectId()), prevPeriod);
                    
                    if (prevLedger != null) {
                        current.setBeginBalance(prevLedger.getEndBalance());
                        current.setBeginDirection(prevLedger.getEndDirection());
                    } else {
                        // 没有上期数据，期初为0
                        current.setBeginBalance(BigDecimal.ZERO);
                        current.setBeginDirection(0);
                    }
                } else {
                    // 从上一期获取期末余额作为期初
                    FinGeneralLedger prev = ledgerList.get(i - 1);
                    current.setBeginBalance(prev.getEndBalance());
                    current.setBeginDirection(prev.getEndDirection());
                }
                
                // 重新计算期末余额：期初余额 + 借方 - 贷方 = 期末余额
                BigDecimal beginBalance = current.getBeginBalance();
                Integer beginDirection = current.getBeginDirection();
                BigDecimal debitTotal = current.getDebitTotal();
                BigDecimal creditTotal = current.getCreditTotal();
                
                // 期初余额已经是带符号的值（借方为正，贷方为负）
                BigDecimal signedBeginBalance = beginBalance;
                
                // 计算期末余额（带符号）
                BigDecimal endBalance = signedBeginBalance.add(debitTotal).subtract(creditTotal);
                
                // 确定期末余额方向和带符号的值
                Integer endDirection;
                if (endBalance.compareTo(BigDecimal.ZERO) > 0) {
                    endDirection = 1; // 借
                } else if (endBalance.compareTo(BigDecimal.ZERO) < 0) {
                    endDirection = 2; // 贷
                    // 保持负数，便于汇总计算
                } else {
                    endDirection = 0; // 平
                }
                
                current.setEndBalance(endBalance);
                current.setEndDirection(endDirection);
                
                finGeneralLedgerMapper.updateFinGeneralLedger(current);
            }
        }
    }



    @Override
    public List<Map<String, Object>> verifyLedgerFormula(String groupid) {
        List<Map<String, Object>> errors = new ArrayList<>();
        
        // 查询所有总账记录
        FinGeneralLedger query = new FinGeneralLedger();
        query.setGroupid(groupid);
        List<FinGeneralLedger> allLedgers = finGeneralLedgerMapper.selectFinGeneralLedgerList(query);
        
        for (FinGeneralLedger ledger : allLedgers) {
            BigDecimal beginBalance = ledger.getBeginBalance() != null ? ledger.getBeginBalance() : BigDecimal.ZERO;
            BigDecimal debitTotal = ledger.getDebitTotal() != null ? ledger.getDebitTotal() : BigDecimal.ZERO;
            BigDecimal creditTotal = ledger.getCreditTotal() != null ? ledger.getCreditTotal() : BigDecimal.ZERO;
            BigDecimal endBalance = ledger.getEndBalance() != null ? ledger.getEndBalance() : BigDecimal.ZERO;
            Integer beginDirection = ledger.getBeginDirection();
            Integer endDirection = ledger.getEndDirection();
            
            // 将期初余额转换为带符号的值（借方为正，贷方为负）
            BigDecimal signedBeginBalance = beginBalance;
            if (beginDirection != null && beginDirection == 2) {
                signedBeginBalance = beginBalance.negate();
            }
            
            // 计算预期的期末余额：期初余额 + 借方 - 贷方
            BigDecimal expectedEndBalance = signedBeginBalance.add(debitTotal).subtract(creditTotal);
            
            // 将实际期末余额转换为带符号的值用于比较
            BigDecimal signedEndBalance = endBalance;
            if (endDirection != null && endDirection == 2) {
                signedEndBalance = endBalance.negate();
            }
            
            // 检查是否相等（允许0.01的误差）
            if (expectedEndBalance.subtract(signedEndBalance).abs().compareTo(new BigDecimal("0.01")) > 0) {
                Map<String, Object> error = new LinkedHashMap<>();
                error.put("ledgerId", ledger.getLedgerId());
                error.put("subjectId", ledger.getSubjectId());
                error.put("period", ledger.getPeriod());
                error.put("beginBalance", beginBalance);
                error.put("beginDirection", beginDirection);
                error.put("debitTotal", debitTotal);
                error.put("creditTotal", creditTotal);
                error.put("endBalance", endBalance);
                error.put("endDirection", endDirection);
                error.put("expectedEndBalance", expectedEndBalance);
                error.put("difference", expectedEndBalance.subtract(signedEndBalance));
                errors.add(error);
            }
        }
        
        return errors;
    }


}
