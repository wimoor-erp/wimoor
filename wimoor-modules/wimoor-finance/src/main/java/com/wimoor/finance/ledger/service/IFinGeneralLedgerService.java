package com.wimoor.finance.ledger.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wimoor.finance.ledger.controller.SubjectBalanceController;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;

/**
 * 总账Service接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface IFinGeneralLedgerService 
{
    /**
     * 查询总账
     * 
     * @param ledgerId 总账主键
     * @return 总账
     */
    public FinGeneralLedger selectFinGeneralLedgerByLedgerId(Long ledgerId);

    /**
     * 查询总账列表
     * 
     * @param finGeneralLedger 总账
     * @return 总账集合
     */
    public List<FinGeneralLedger> selectFinGeneralLedgerList(FinGeneralLedger finGeneralLedger);
    public List<Map<String,Object>> selectList(FinLedgerDTO finGeneralLedger);
    /**
     * 新增总账
     * 
     * @param finGeneralLedger 总账
     * @return 结果
     */
    public int insertFinGeneralLedger(FinGeneralLedger finGeneralLedger);

    /**
     * 修改总账
     * 
     * @param finGeneralLedger 总账
     * @return 结果
     */
    public int updateFinGeneralLedger(FinGeneralLedger finGeneralLedger);

    /**
     * 批量删除总账
     * 
     * @param ledgerIds 需要删除的总账主键集合
     * @return 结果
     */
    public int deleteFinGeneralLedgerByLedgerIds(Long[] ledgerIds);

    /**
     * 删除总账信息
     * 
     * @param ledgerId 总账主键
     * @return 结果
     */
    public int deleteFinGeneralLedgerByLedgerId(Long ledgerId);

    /**
     * 查询总账信息
     *
     * @param groupid 集团ID
     * @param subjectId 会计科目ID
     * @param period 会计期间
     * @return 总账信息
     */
    FinGeneralLedger selectBySubjectAndPeriod(String groupid, String subjectId, String period);

    FinGeneralLedger selectBySubjectCodeAndPeriod(String groupid, String subjectCodes, String period);

    int deleteByPeriod(String groupid, String nextPeriod);

    List<Map<String, Object>> getAllSubjectBalance(String groupid, String period);
}
