package com.wimoor.finance.voucher.service;

import java.util.List;
import com.wimoor.finance.voucher.domain.FinVoucherModifyLog;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.VoucherBackup;

/**
 * 凭证修改日志Service接口
 * 
 * @author wimoor
 * @date 2025-11-22
 */
public interface IFinVoucherModifyLogService 
{
    /**
     * 查询凭证修改日志
     * 
     * @param logId 凭证修改日志主键
     * @return 凭证修改日志
     */
    public FinVoucherModifyLog selectFinVoucherModifyLogByLogId(String logId);

    /**
     * 查询凭证修改日志列表
     * 
     * @param finVoucherModifyLog 凭证修改日志
     * @return 凭证修改日志集合
     */
    public List<FinVoucherModifyLog> selectFinVoucherModifyLogList(FinVoucherModifyLog finVoucherModifyLog);

    /**
     * 新增凭证修改日志
     * 
     * @param finVoucherModifyLog 凭证修改日志
     * @return 结果
     */
    public int insertFinVoucherModifyLog(FinVoucherModifyLog finVoucherModifyLog);

    /**
     * 修改凭证修改日志
     * 
     * @param finVoucherModifyLog 凭证修改日志
     * @return 结果
     */
    public int updateFinVoucherModifyLog(FinVoucherModifyLog finVoucherModifyLog);

    /**
     * 批量删除凭证修改日志
     * 
     * @param logIds 需要删除的凭证修改日志主键集合
     * @return 结果
     */
    public int deleteFinVoucherModifyLogByLogIds(String[] logIds);

    /**
     * 删除凭证修改日志信息
     * 
     * @param logId 凭证修改日志主键
     * @return 结果
     */
    public int deleteFinVoucherModifyLogByLogId(String logId);

    public void logModification(VoucherBackup backup, FinVouchers modifiedVoucher,
                                String modifyBy, String reason);
}
