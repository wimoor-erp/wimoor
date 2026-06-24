package com.wimoor.finance.voucher.service.impl;

import com.wimoor.finance.voucher.domain.FinVoucherEntriesAuxiliary;
import com.wimoor.finance.voucher.mapper.FinVoucherEntriesAuxiliaryMapper;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesAuxiliaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 凭证分录辅助核算Service业务层处理
 * 
 * @author wimoor
 * @date 2026-05-26
 */
@Service
public class FinVoucherEntriesAuxiliaryServiceImpl implements IFinVoucherEntriesAuxiliaryService 
{
    @Autowired
    private FinVoucherEntriesAuxiliaryMapper finVoucherEntriesAuxiliaryMapper;

    /**
     * 查询凭证分录辅助核算
     * 
     * @param id 凭证分录辅助核算主键
     * @return 凭证分录辅助核算
     */
    @Override
    public FinVoucherEntriesAuxiliary selectFinVoucherEntriesAuxiliaryById(String id)
    {
        return finVoucherEntriesAuxiliaryMapper.selectFinVoucherEntriesAuxiliaryById(id);
    }

    /**
     * 查询凭证分录辅助核算列表
     * 
     * @param finVoucherEntriesAuxiliary 凭证分录辅助核算
     * @return 凭证分录辅助核算
     */
    @Override
    public List<FinVoucherEntriesAuxiliary> selectFinVoucherEntriesAuxiliaryList(FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary)
    {
        return finVoucherEntriesAuxiliaryMapper.selectFinVoucherEntriesAuxiliaryList(finVoucherEntriesAuxiliary);
    }

    /**
     * 新增凭证分录辅助核算
     * 
     * @param finVoucherEntriesAuxiliary 凭证分录辅助核算
     * @return 结果
     */
    @Override
    public int insertFinVoucherEntriesAuxiliary(FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary)
    {
        return finVoucherEntriesAuxiliaryMapper.insertFinVoucherEntriesAuxiliary(finVoucherEntriesAuxiliary);
    }

    /**
     * 修改凭证分录辅助核算
     * 
     * @param finVoucherEntriesAuxiliary 凭证分录辅助核算
     * @return 结果
     */
    @Override
    public int updateFinVoucherEntriesAuxiliary(FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary)
    {
        return finVoucherEntriesAuxiliaryMapper.updateFinVoucherEntriesAuxiliary(finVoucherEntriesAuxiliary);
    }

    /**
     * 批量删除凭证分录辅助核算
     * 
     * @param ids 需要删除的凭证分录辅助核算主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherEntriesAuxiliaryByIds(String[] ids)
    {
        return finVoucherEntriesAuxiliaryMapper.deleteFinVoucherEntriesAuxiliaryByIds(ids);
    }

    /**
     * 删除凭证分录辅助核算信息
     * 
     * @param id 凭证分录辅助核算主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherEntriesAuxiliaryById(String id)
    {
        return finVoucherEntriesAuxiliaryMapper.deleteFinVoucherEntriesAuxiliaryById(id);
    }

    @Override
    public int deleteByEntryId(Long entryId) {
        return finVoucherEntriesAuxiliaryMapper.deleteByEntryId(entryId);
    }

    @Override
    public int deleteByVoucherId(Long voucherId) {
        return finVoucherEntriesAuxiliaryMapper.deleteByVoucherId(voucherId);
    }

    @Override
    public int batchInsert(List<FinVoucherEntriesAuxiliary> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return finVoucherEntriesAuxiliaryMapper.batchInsert(list);
    }

    @Override
    public List<FinVoucherEntriesAuxiliary> selectByEntryIds(List<Long> entryIds) {
        if (entryIds == null || entryIds.isEmpty()) {
            return Collections.emptyList();
        }
        return finVoucherEntriesAuxiliaryMapper.selectByEntryIds(entryIds);
    }
}
