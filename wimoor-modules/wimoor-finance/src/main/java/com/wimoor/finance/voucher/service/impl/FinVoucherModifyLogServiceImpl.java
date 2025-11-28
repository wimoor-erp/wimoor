package com.wimoor.finance.voucher.service.impl;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.VoucherBackup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.voucher.mapper.FinVoucherModifyLogMapper;
import com.wimoor.finance.voucher.domain.FinVoucherModifyLog;
import com.wimoor.finance.voucher.service.IFinVoucherModifyLogService;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
/**
 * 凭证修改日志Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-22
 */
@Service
public class FinVoucherModifyLogServiceImpl implements IFinVoucherModifyLogService 
{
    @Autowired
    private FinVoucherModifyLogMapper finVoucherModifyLogMapper;

    /**
     * 查询凭证修改日志
     * 
     * @param logId 凭证修改日志主键
     * @return 凭证修改日志
     */
    @Override
    public FinVoucherModifyLog selectFinVoucherModifyLogByLogId(String logId)
    {
        return finVoucherModifyLogMapper.selectFinVoucherModifyLogByLogId(logId);
    }

    /**
     * 查询凭证修改日志列表
     * 
     * @param finVoucherModifyLog 凭证修改日志
     * @return 凭证修改日志
     */
    @Override
    public List<FinVoucherModifyLog> selectFinVoucherModifyLogList(FinVoucherModifyLog finVoucherModifyLog)
    {
        return finVoucherModifyLogMapper.selectFinVoucherModifyLogList(finVoucherModifyLog);
    }

    /**
     * 新增凭证修改日志
     * 
     * @param finVoucherModifyLog 凭证修改日志
     * @return 结果
     */
    @Override
    public int insertFinVoucherModifyLog(FinVoucherModifyLog finVoucherModifyLog)
    {
        return finVoucherModifyLogMapper.insertFinVoucherModifyLog(finVoucherModifyLog);
    }

    /**
     * 修改凭证修改日志
     * 
     * @param finVoucherModifyLog 凭证修改日志
     * @return 结果
     */
    @Override
    public int updateFinVoucherModifyLog(FinVoucherModifyLog finVoucherModifyLog)
    {
        return finVoucherModifyLogMapper.updateFinVoucherModifyLog(finVoucherModifyLog);
    }

    /**
     * 批量删除凭证修改日志
     * 
     * @param logIds 需要删除的凭证修改日志主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherModifyLogByLogIds(String[] logIds)
    {
        return finVoucherModifyLogMapper.deleteFinVoucherModifyLogByLogIds(logIds);
    }

    /**
     * 删除凭证修改日志信息
     * 
     * @param logId 凭证修改日志主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherModifyLogByLogId(String logId)
    {
        return finVoucherModifyLogMapper.deleteFinVoucherModifyLogByLogId(logId);
    }


    /**
     * 记录凭证修改痕迹
     */
    public void logModification(VoucherBackup backup, FinVouchers modifiedVoucher,
                                String modifyBy, String reason) {
        FinVoucherModifyLog log = new FinVoucherModifyLog();
        log.setGroupid(modifiedVoucher.getGroupid());
        log.setVoucherId(String.valueOf(modifiedVoucher.getVoucherId()));
        log.setModifyType(2); // 修改
        log.setModifyBy(modifyBy);
        log.setModifyTime(new Date());
        log.setReason(reason);

        // 记录修改前后数据对比
        log.setBeforeData(serializeBackupData(backup));
        log.setAfterData(serializeCurrentData(modifiedVoucher));

        finVoucherModifyLogMapper.insertFinVoucherModifyLog(log);
    }

    private String serializeCurrentData(FinVouchers modifiedVoucher) {
        return JSON.toJSONString(modifiedVoucher);
    }

    /**
     * 序列化备份数据
     */
    private String serializeBackupData(VoucherBackup backup) {
        Map<String, Object> beforeData = new HashMap<String, Object>();
        beforeData.put("voucher", backup.getOriginalVoucher());
        beforeData.put("entries", backup.getOriginalEntries());
        beforeData.put("ledgerEntries", backup.getOriginalLedgerEntries());
        return JSON.toJSONString(beforeData);
    }
}
