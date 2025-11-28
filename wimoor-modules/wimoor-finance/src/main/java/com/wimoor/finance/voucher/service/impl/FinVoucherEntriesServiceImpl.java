package com.wimoor.finance.voucher.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.voucher.mapper.FinVoucherEntriesMapper;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesService;

/**
 * 凭证分录明细Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinVoucherEntriesServiceImpl implements IFinVoucherEntriesService 
{
    @Autowired
    private FinVoucherEntriesMapper finVoucherEntriesMapper;

    /**
     * 查询凭证分录明细
     * 
     * @param entryId 凭证分录明细主键
     * @return 凭证分录明细
     */
    @Override
    public FinVoucherEntries selectFinVoucherEntriesByEntryId(Long entryId)
    {
        return finVoucherEntriesMapper.selectFinVoucherEntriesByEntryId(entryId);
    }

    /**
     * 查询凭证分录明细列表
     * 
     * @param finVoucherEntries 凭证分录明细
     * @return 凭证分录明细
     */
    @Override
    public List<FinVoucherEntries> selectFinVoucherEntriesList(FinVoucherEntries finVoucherEntries)
    {
        return finVoucherEntriesMapper.selectFinVoucherEntriesList(finVoucherEntries);
    }

    @Override
    public List<FinVoucherEntries> selectFinVoucherEntriesListByVoucherId(Long voucherId) {
        return this.finVoucherEntriesMapper.selectByVoucherId(voucherId);
    }


    /**
     * 新增凭证分录明细
     * 
     * @param finVoucherEntries 凭证分录明细
     * @return 结果
     */
    @Override
    public int insertFinVoucherEntries(FinVoucherEntries finVoucherEntries)
    {
        return finVoucherEntriesMapper.insertFinVoucherEntries(finVoucherEntries);
    }

    /**
     * 修改凭证分录明细
     * 
     * @param finVoucherEntries 凭证分录明细
     * @return 结果
     */
    @Override
    public int updateFinVoucherEntries(FinVoucherEntries finVoucherEntries)
    {
        return finVoucherEntriesMapper.updateFinVoucherEntries(finVoucherEntries);
    }

    /**
     * 批量删除凭证分录明细
     * 
     * @param entryIds 需要删除的凭证分录明细主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherEntriesByEntryIds(Long[] entryIds)
    {
        return finVoucherEntriesMapper.deleteFinVoucherEntriesByEntryIds(entryIds);
    }

    /**
     * 删除凭证分录明细信息
     * 
     * @param entryId 凭证分录明细主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherEntriesByEntryId(Long entryId)
    {
        return finVoucherEntriesMapper.deleteFinVoucherEntriesByEntryId(entryId);
    }

    @Override
    public int deleteByVoucherId(Long voucherId) {
        return finVoucherEntriesMapper.deleteByVoucherId(voucherId);
    }
}
