package com.wimoor.finance.voucher.mapper;

import java.util.List;
import com.wimoor.finance.voucher.domain.FinVoucherModifyLog;

/**
 * 凭证修改日志Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-22
 */
public interface FinVoucherModifyLogMapper 
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
     * 删除凭证修改日志
     * 
     * @param logId 凭证修改日志主键
     * @return 结果
     */
    public int deleteFinVoucherModifyLogByLogId(String logId);

    /**
     * 批量删除凭证修改日志
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinVoucherModifyLogByLogIds(String[] logIds);
}
