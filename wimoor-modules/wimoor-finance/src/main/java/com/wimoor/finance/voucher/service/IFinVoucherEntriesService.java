package com.wimoor.finance.voucher.service;

import java.util.List;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;

/**
 * 凭证分录明细Service接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface IFinVoucherEntriesService 
{
    /**
     * 查询凭证分录明细
     * 
     * @param entryId 凭证分录明细主键
     * @return 凭证分录明细
     */
    public FinVoucherEntries selectFinVoucherEntriesByEntryId(Long entryId);

    /**
     * 查询凭证分录明细列表
     * 
     * @param finVoucherEntries 凭证分录明细
     * @return 凭证分录明细集合
     */
    public List<FinVoucherEntries> selectFinVoucherEntriesList(FinVoucherEntries finVoucherEntries);
    public List<FinVoucherEntries> selectFinVoucherEntriesListByVoucherId(Long voucherId);

    /**
     * 新增凭证分录明细
     * 
     * @param finVoucherEntries 凭证分录明细
     * @return 结果
     */
    public int insertFinVoucherEntries(FinVoucherEntries finVoucherEntries);

    /**
     * 修改凭证分录明细
     * 
     * @param finVoucherEntries 凭证分录明细
     * @return 结果
     */
    public int updateFinVoucherEntries(FinVoucherEntries finVoucherEntries);

    /**
     * 批量删除凭证分录明细
     * 
     * @param entryIds 需要删除的凭证分录明细主键集合
     * @return 结果
     */
    public int deleteFinVoucherEntriesByEntryIds(Long[] entryIds);

    /**
     * 删除凭证分录明细信息
     * 
     * @param entryId 凭证分录明细主键
     * @return 结果
     */
    public int deleteFinVoucherEntriesByEntryId(Long entryId);

    int deleteByVoucherId(Long voucherId);


}
