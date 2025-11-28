package com.wimoor.finance.ledger.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wimoor.finance.ledger.domain.FinDetailLedger;
import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;

/**
 * 明细账表Service接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface IFinDetailLedgerService 
{
    /**
     * 查询明细账表
     * 
     * @param detailId 明细账表主键
     * @return 明细账表
     */
    public FinDetailLedger selectFinDetailLedgerByDetailId(Long detailId);

    /**
     * 查询明细账表列表
     * 
     * @param finDetailLedger 明细账表
     * @return 明细账表集合
     */
    public List<FinDetailLedger> selectFinDetailLedgerList(FinDetailLedger finDetailLedger);
    public List<Map<String,Object>> selectList(FinLedgerDTO finDetailLedger);
    /**
     * 新增明细账表
     * 
     * @param finDetailLedger 明细账表
     * @return 结果
     */
    public int insertFinDetailLedger(FinDetailLedger finDetailLedger);

    /**
     * 修改明细账表
     * 
     * @param finDetailLedger 明细账表
     * @return 结果
     */
    public int updateFinDetailLedger(FinDetailLedger finDetailLedger);

    /**
     * 批量删除明细账表
     * 
     * @param detailIds 需要删除的明细账表主键集合
     * @return 结果
     */
    public int deleteFinDetailLedgerByDetailIds(Long[] detailIds);

    /**
     * 删除明细账表信息
     * 
     * @param detailId 明细账表主键
     * @return 结果
     */
    public int deleteFinDetailLedgerByDetailId(Long detailId);

    void deleteByVoucherId(Long voucherId);

    FinDetailLedger selectLastDetailLedger(String groupid, String subjectId, Date voucherDate);

    List<FinDetailLedger> selectByVoucherId(Long voucherId);
}
