package com.wimoor.finance.ledger.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.ledger.mapper.FinGeneralLedgerMapper;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;

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


}
