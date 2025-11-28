package com.wimoor.finance.voucher.mapper;

import java.util.List;

import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.FinVourchesFile;

/**
 * 凭证附件Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-27
 */
public interface FinVourchesFileMapper 
{
    /**
     * 查询凭证附件
     * 
     * @param id 凭证附件主键
     * @return 凭证附件
     */
    public FinVourchesFile selectFinVourchesFileById(String id);

    /**
     * 查询凭证附件列表
     * 
     * @param finVourchesFile 凭证附件
     * @return 凭证附件集合
     */
    public List<FinVourchesFile> selectFinVourchesFileList(FinVourchesFile finVourchesFile);

    /**
     * 新增凭证附件
     * 
     * @param finVourchesFile 凭证附件
     * @return 结果
     */
    public int insertFinVourchesFile(FinVourchesFile finVourchesFile);

    /**
     * 修改凭证附件
     * 
     * @param finVourchesFile 凭证附件
     * @return 结果
     */
    public int updateFinVourchesFile(FinVourchesFile finVourchesFile);

    /**
     * 删除凭证附件
     * 
     * @param id 凭证附件主键
     * @return 结果
     */
    public int deleteFinVourchesFileById(String id);

    /**
     * 批量删除凭证附件
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinVourchesFileByIds(String[] ids);

    int deleteFinVourchesFileByVoucherId(FinVouchers finVouchers);

    List<FinVourchesFile> selectFinVourchesFileByVoucherId(FinVouchers finVouchers);
}
