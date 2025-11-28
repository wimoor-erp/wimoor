package com.wimoor.finance.voucher.mapper;

import java.util.List;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.FinVouchers;
import org.apache.ibatis.annotations.Param;

/**
 * 凭证分录明细Mapper接口
 *
 * @author wimoor
 * @date 2025-11-04
 */
public interface FinVoucherEntriesMapper
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
     * 删除凭证分录明细
     *
     * @param entryId 凭证分录明细主键
     * @return 结果
     */
    public int deleteFinVoucherEntriesByEntryId(Long entryId);

    /**
     * 批量删除凭证分录明细
     *
     * @param entryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinVoucherEntriesByEntryIds(Long[] entryIds);

    /**
     * 删除凭证分录明细
     *
     * @param entries 凭证分录明细主键
     * @return 结果
     */
    public int deleteFinVoucherEntriesByVoucherId(FinVouchers entries);

    /**
     * 根据凭证ID查询分录列表
     */
    List<FinVoucherEntries> selectByVoucherId(@Param("voucherId") Long voucherId);

    int deleteByVoucherId(@Param("voucherId")Long voucherId);
}
