package com.wimoor.finance.setting.service.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;
import com.wimoor.finance.setting.mapper.FinAccountingSubjectsMapper;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;

/**
 * 会计科目Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-05
 */
@Service
public class FinAccountingSubjectsServiceImpl implements IFinAccountingSubjectsService 
{
    @Autowired
    private FinAccountingSubjectsMapper finAccountingSubjectsMapper;
    @Autowired
    private IFinAccountingPeriodsService finAccountingPeriodsService;
    /**
     * 查询会计科目
     * 
     * @param subjectId 会计科目主键
     * @return 会计科目
     */
    @Override
    public FinAccountingSubjects selectFinAccountingSubjectsBySubjectId(String subjectId)
    {
        return finAccountingSubjectsMapper.selectFinAccountingSubjectsBySubjectId(subjectId);
    }

    /**
     * 查询会计科目列表
     * 
     * @param finAccountingSubjects 会计科目
     * @return 会计科目
     */
    @Override
    public List<FinAccountingSubjects> selectFinAccountingSubjectsList(FinAccountingSubjects finAccountingSubjects)
    {
        return finAccountingSubjectsMapper.selectFinAccountingSubjectsList(finAccountingSubjects);
    }


    @Override
    public int initFinAccountingSubjects(String groupId) {
        finAccountingPeriodsService.initAccountingPeriods(groupId);
        return finAccountingSubjectsMapper.initFin(groupId);
    }

    /**
     * 获取所有损益类科目（基于科目编码识别）
     * 通常损益类科目的编码以 6 开头（收入类）和 5 开头（费用类）
     */
    public List<FinAccountingSubjects> getProfitLossSubjects(String groupid) {
        // 损益类科目通常包括：
        // - 5开头：费用类科目（如：5601销售费用、5602管理费用等）
        // - 6开头：收入类科目（如：6001主营业务收入、6051其他业务收入等）
        List<String> profitLossPrefixes = Arrays.asList("5", "6");
        return finAccountingSubjectsMapper.selectBySubjectCodePrefixes(groupid, profitLossPrefixes);
    }

    /**
     * 获取income科目（基于科目编码识别）
     * 收入类科目编码通常以 6 开头
     */
    public List<FinAccountingSubjects> getIncomeSubjects(String groupid) {
        // 收入类科目编码前缀：6
        List<String> incomePrefixes = Arrays.asList("6");
        return finAccountingSubjectsMapper.selectBySubjectCodePrefixes(groupid, incomePrefixes);
    }

    /**
     * 获取费用类科目（基于科目编码识别）
     * 费用类科目编码通常以 5 开头
     */
    public List<FinAccountingSubjects> getExpenseSubjects(String groupid) {
        // 费用类科目编码前缀：5
        List<String> expensePrefixes = Arrays.asList("5");
        return finAccountingSubjectsMapper.selectBySubjectCodePrefixes(groupid, expensePrefixes);
    }

    /**
     * 获取主营业务收入科目（更精确的编码匹配）
     */
    public FinAccountingSubjects getMainBusinessIncomeSubjects(String groupid) {
        // 主营业务收入科目：6001
        return finAccountingSubjectsMapper.selectBySubjectCode(groupid, "6001");
    }

    /**
     * 获取期间费用科目
     */
    public List<FinAccountingSubjects> getPeriodExpenseSubjects(String groupid) {
        // 期间费用科目：5601销售费用、5602管理费用、5603财务费用
        List<String> periodExpenseCodes = Arrays.asList("5601", "5602", "5603");
        return finAccountingSubjectsMapper.selectBySubjectCodes(groupid, periodExpenseCodes);
    }

    /**
     * 获取成本类科目（基于科目编码识别）
     * 成本类科目编码通常以 4 开头
     */
    public List<FinAccountingSubjects> getCostSubjects(String groupid) {
        // 成本类科目编码前缀：4
        List<String> costPrefixes = Arrays.asList("4");
        return finAccountingSubjectsMapper.selectBySubjectCodePrefixes(groupid, costPrefixes);
    }
    /**
     * 新增会计科目
     * 
     * @param finAccountingSubjects 会计科目
     * @return 结果
     */
    @Override
    public int insertFinAccountingSubjects(FinAccountingSubjects finAccountingSubjects)
    {
        return finAccountingSubjectsMapper.insertFinAccountingSubjects(finAccountingSubjects);
    }

    /**
     * 修改会计科目
     * 
     * @param finAccountingSubjects 会计科目
     * @return 结果
     */
    @Override
    public int updateFinAccountingSubjects(FinAccountingSubjects finAccountingSubjects)
    {
        return finAccountingSubjectsMapper.updateFinAccountingSubjects(finAccountingSubjects);
    }

    /**
     * 批量删除会计科目
     * 
     * @param subjectIds 需要删除的会计科目主键
     * @return 结果
     */
    @Override
    public int deleteFinAccountingSubjectsBySubjectIds(String[] subjectIds)
    {
        return finAccountingSubjectsMapper.deleteFinAccountingSubjectsBySubjectIds(subjectIds);
    }

    /**
     * 删除会计科目信息
     * 
     * @param subjectId 会计科目主键
     * @return 结果
     */
    @Override
    public int deleteFinAccountingSubjectsBySubjectId(String subjectId)
    {
        return finAccountingSubjectsMapper.deleteFinAccountingSubjectsBySubjectId(subjectId);
    }

    @Override
    public FinAccountingSubjects getSubjectById(String subjectId) {
        // 获取科目信息
        FinAccountingSubjects subject = finAccountingSubjectsMapper.selectFinAccountingSubjectsBySubjectId(subjectId);
        return subject;
    }

    /**
     * 获取本年利润科目
     * 本年利润科目通常编码为 4103
     */
    @Override
    public FinAccountingSubjects getProfitSubject(String groupid) {
        // 本年利润科目：4103
        return finAccountingSubjectsMapper.selectBySubjectCode(groupid, "4103");
    }

    /**
     * 获取利润分配科目
     * 利润分配科目通常编码为 4104
     */
    @Override
    public FinAccountingSubjects getProfitDistributionSubject(String groupid) {
        // 利润分配科目：4104
        return finAccountingSubjectsMapper.selectBySubjectCode(groupid, "4104");
    }
}