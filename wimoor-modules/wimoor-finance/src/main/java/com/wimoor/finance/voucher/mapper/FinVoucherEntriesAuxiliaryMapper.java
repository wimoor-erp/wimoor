package com.wimoor.finance.voucher.mapper;

import com.wimoor.finance.voucher.domain.FinVoucherEntriesAuxiliary;

import java.util.List;

/**
 * 凭证分录辅助核算Mapper接口
 * 
 * @author wimoor
 * @date 2026-05-26
 */
public interface FinVoucherEntriesAuxiliaryMapper 
{
    /**
     * 查询凭证分录辅助核算
     * 
     * @param id 凭证分录辅助核算主键
     * @return 凭证分录辅助核算
     */
    public FinVoucherEntriesAuxiliary selectFinVoucherEntriesAuxiliaryById(String id);

    /**
     * 查询凭证分录辅助核算列表
     * 
     * @param finVoucherEntriesAuxiliary 凭证分录辅助核算
     * @return 凭证分录辅助核算集合
     */
    public List<FinVoucherEntriesAuxiliary> selectFinVoucherEntriesAuxiliaryList(FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary);

    /**
     * 新增凭证分录辅助核算
     * 
     * @param finVoucherEntriesAuxiliary 凭证分录辅助核算
     * @return 结果
     */
    public int insertFinVoucherEntriesAuxiliary(FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary);

    /**
     * 修改凭证分录辅助核算
     * 
     * @param finVoucherEntriesAuxiliary 凭证分录辅助核算
     * @return 结果
     */
    public int updateFinVoucherEntriesAuxiliary(FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary);

    /**
     * 删除凭证分录辅助核算
     * 
     * @param id 凭证分录辅助核算主键
     * @return 结果
     */
    public int deleteFinVoucherEntriesAuxiliaryById(String id);

    /**
     * 批量删除凭证分录辅助核算
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinVoucherEntriesAuxiliaryByIds(String[] ids);

    /**
     * 根据分录ID删除辅助核算
     */
    public int deleteByEntryId(Long entryId);

    /**
     * 根据凭证ID删除所有辅助核算
     */
    public int deleteByVoucherId(Long voucherId);

    /**
     * 批量插入辅助核算
     */
    public int batchInsert(List<FinVoucherEntriesAuxiliary> list);

    /**
     * 根据分录ID列表查询辅助核算
     */
    public List<FinVoucherEntriesAuxiliary> selectByEntryIds(List<Long> entryIds);
}
