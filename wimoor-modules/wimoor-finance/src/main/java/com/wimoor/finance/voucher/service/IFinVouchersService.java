package com.wimoor.finance.voucher.service;

import java.util.Date;
import java.util.List;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.dto.FinVoucherDTO;
import com.wimoor.finance.voucher.service.util.BatchPostingResult;

/**
 * 记账凭证Service接口
 *
 * @author wimoor
 * @date 2025-11-04
 */
public interface IFinVouchersService
{
    /**
     * 查询记账凭证
     *
     * @param voucherId 记账凭证主键
     * @return 记账凭证
     */
    FinVouchers selectFinVouchersByVoucherId(Long voucherId);

    /**
     * 查询记账凭证列表
     *
     * @param finVouchers 记账凭证
     * @return 记账凭证集合
     */
    public List<FinVouchers> selectFinVouchersList(FinVoucherDTO finVouchers);

    String selectNextVoucherNo(FinVouchers finVouchers);

    /**
     * 新增记账凭证
     *
     * @param finVouchers 记账凭证
     * @return 结果
     */
    public int insertFinVouchers(FinVouchers finVouchers);

    /**
     * 修改记账凭证
     *
     * @param finVouchers 记账凭证
     * @return 结果
     */
    public int updateFinVouchers(FinVouchers finVouchers);

    /**
     * 批量删除记账凭证
     *
     * @param voucherIds 需要删除的记账凭证主键集合
     * @return 结果
     */
    public int deleteFinVouchersByVoucherIds(Long[] voucherIds);

    /**
     * 删除记账凭证信息
     *
     * @param voucherId 记账凭证主键
     * @return 结果
     */
    public int deleteFinVouchersByVoucherId(Long voucherId);
    /**
     * 审核记账凭证
     *
     * @param finVoucherIds 记账凭证IDS
     * @return 结果
     */
    int approveFinVouchers(List<Long> finVoucherIds);

    BatchPostingResult batchPostVouchers(List<Long> finVoucherIds);

    Integer countProfitTransferVouchers(String groupid, String period);

    List<FinVouchers> selectVouchersModifiedAfterClosing(String groupid, String period, Date closeTime);

    List<FinVouchers> selectProfitTransferVouchers(String groupid, String period);

    public void reversePosting(Long voucherId);

    int filesFinVouchers(FinVouchers finVouchers);
}
