package com.wimoor.finance.ledger.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.ledger.mapper.FinDetailLedgerMapper;
import com.wimoor.finance.ledger.domain.FinDetailLedger;
import com.wimoor.finance.ledger.service.IFinDetailLedgerService;

/**
 * 明细账表Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinDetailLedgerServiceImpl implements IFinDetailLedgerService 
{
    @Autowired
    private FinDetailLedgerMapper finDetailLedgerMapper;

    /**
     * 查询明细账表
     * 
     * @param detailId 明细账表主键
     * @return 明细账表
     */
    @Override
    public FinDetailLedger selectFinDetailLedgerByDetailId(Long detailId)
    {
        return finDetailLedgerMapper.selectFinDetailLedgerByDetailId(detailId);
    }

    /**
     * 查询明细账表列表
     * 
     * @param finDetailLedger 明细账表
     * @return 明细账表
     */
    @Override
    public List<FinDetailLedger> selectFinDetailLedgerList(FinDetailLedger finDetailLedger)
    {
        return finDetailLedgerMapper.selectFinDetailLedgerList(finDetailLedger);
    }

    /**
     * 查询明细账表列表
     *
     * @param finDetailLedger 明细账表
     * @return 明细账表
     */
    @Override
    public List<Map<String,Object>> selectList(FinLedgerDTO finDetailLedger)
    {
        return finDetailLedgerMapper.selectList(finDetailLedger);
    }

    @Override
    public List<Map<String,Object>> selectSubjectTree(FinLedgerDTO finDetailLedger)
    {
        return finDetailLedgerMapper.subjectTree(finDetailLedger);
    }


    /**
     * 新增明细账表
     * 
     * @param finDetailLedger 明细账表
     * @return 结果
     */
    @Override
    public int insertFinDetailLedger(FinDetailLedger finDetailLedger)
    {
        return finDetailLedgerMapper.insertFinDetailLedger(finDetailLedger);
    }

    /**
     * 修改明细账表
     * 
     * @param finDetailLedger 明细账表
     * @return 结果
     */
    @Override
    public int updateFinDetailLedger(FinDetailLedger finDetailLedger)
    {
        return finDetailLedgerMapper.updateFinDetailLedger(finDetailLedger);
    }

    /**
     * 批量删除明细账表
     * 
     * @param detailIds 需要删除的明细账表主键
     * @return 结果
     */
    @Override
    public int deleteFinDetailLedgerByDetailIds(Long[] detailIds)
    {
        return finDetailLedgerMapper.deleteFinDetailLedgerByDetailIds(detailIds);
    }

    /**
     * 删除明细账表信息
     * 
     * @param detailId 明细账表主键
     * @return 结果
     */
    @Override
    public int deleteFinDetailLedgerByDetailId(Long detailId)
    {
        return finDetailLedgerMapper.deleteFinDetailLedgerByDetailId(detailId);
    }

    @Override
    public void deleteByVoucherId(Long voucherId) {
        finDetailLedgerMapper.deleteByVoucherId(voucherId);
    }

    @Override
    public FinDetailLedger selectLastDetailLedger(String groupid, String subjectId, Date voucherDate) {
        return finDetailLedgerMapper.selectLastDetailLedger(groupid, subjectId, voucherDate);
    }

    @Override
    public List<FinDetailLedger> selectByVoucherId(Long voucherId) {
        return finDetailLedgerMapper.selectByVoucherId(voucherId);
    }

    @Override
    public List<FinDetailLedger> selectLatestBalanceBeforeDate(String groupid, String subjectCode, LocalDate startDate) {
        return finDetailLedgerMapper.selectLatestBalanceBeforeDate(groupid, subjectCode, startDate);
    }

    @Override
    public BigDecimal sumChildSubjectsOpeningBalance(String groupid, String parentSubjectCode, LocalDate startDate) {
        return finDetailLedgerMapper.sumChildSubjectsOpeningBalance(groupid, parentSubjectCode, startDate);
    }
}
