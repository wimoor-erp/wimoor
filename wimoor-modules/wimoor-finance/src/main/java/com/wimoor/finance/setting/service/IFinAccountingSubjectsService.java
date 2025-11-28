package com.wimoor.finance.setting.service;

import java.util.Arrays;
import java.util.List;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;

/**
 * 会计科目Service接口
 * 
 * @author wimoor
 * @date 2025-11-05
 */
public interface IFinAccountingSubjectsService 
{
    /**
     * 查询会计科目
     * 
     * @param subjectId 会计科目主键
     * @return 会计科目
     */
    public FinAccountingSubjects selectFinAccountingSubjectsBySubjectId(String subjectId);

    /**
     * 查询会计科目列表
     * 
     * @param finAccountingSubjects 会计科目
     * @return 会计科目集合
     */
    public List<FinAccountingSubjects> selectFinAccountingSubjectsList(FinAccountingSubjects finAccountingSubjects);

    /**
     * 新增会计科目
     * 
     * @param finAccountingSubjects 会计科目
     * @return 结果
     */
    public int insertFinAccountingSubjects(FinAccountingSubjects finAccountingSubjects);

    /**
     * 修改会计科目
     * 
     * @param finAccountingSubjects 会计科目
     * @return 结果
     */
    public int updateFinAccountingSubjects(FinAccountingSubjects finAccountingSubjects);

    /**
     * 批量删除会计科目
     * 
     * @param subjectIds 需要删除的会计科目主键集合
     * @return 结果
     */
    public int deleteFinAccountingSubjectsBySubjectIds(String[] subjectIds);

    /**
     * 删除会计科目信息
     * 
     * @param subjectId 会计科目主键
     * @return 结果
     */
    public int deleteFinAccountingSubjectsBySubjectId(String subjectId);

    public FinAccountingSubjects getSubjectById(String currentSubjectId);

    /**
     * 初始化会计科目
     *
     * @param groupId 集团ID
     * @return 结果
     */
    public int initFinAccountingSubjects(String groupId);

    List<FinAccountingSubjects> getProfitLossSubjects(String groupid);


    /**
     * 获取收入类科目（基于科目编码识别）
     * 收入类科目编码通常以 6 开头
     */
    List<FinAccountingSubjects> getIncomeSubjects(String groupid) ;

    /**
     * 获取费用类科目（基于科目编码识别）
     * 费用类科目编码通常以 5 开头
     */
    List<FinAccountingSubjects> getExpenseSubjects(String groupid);

    /**
     * 获取主营业务收入科目（更精确的编码匹配）
     */
    FinAccountingSubjects getMainBusinessIncomeSubjects(String groupid) ;

    /**
     * 获取期间费用科目
     */
    List<FinAccountingSubjects> getPeriodExpenseSubjects(String groupid) ;

    /**
     * 获取成本类科目（基于科目编码识别）
     * 成本类科目编码通常以 4 开头
     */
    List<FinAccountingSubjects> getCostSubjects(String groupid);

    /**
     * 获取本年利润科目
     */
    FinAccountingSubjects getProfitSubject(String groupid);

    /**
     * 获取利润分配科目
     */
    FinAccountingSubjects getProfitDistributionSubject(String groupid);
}