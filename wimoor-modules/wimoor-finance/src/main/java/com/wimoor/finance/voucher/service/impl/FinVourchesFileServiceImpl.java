package com.wimoor.finance.voucher.service.impl;

import java.util.List;

import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.FinVourchesFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.voucher.mapper.FinVourchesFileMapper;
import com.wimoor.finance.voucher.service.IFinVourchesFileService;

/**
 * 凭证附件Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-27
 */
@Service
public class FinVourchesFileServiceImpl implements IFinVourchesFileService 
{
    @Autowired
    private FinVourchesFileMapper finVourchesFileMapper;

    /**
     * 查询凭证附件
     * 
     * @param id 凭证附件主键
     * @return 凭证附件
     */
    @Override
    public FinVourchesFile selectFinVourchesFileById(String id)
    {
        return finVourchesFileMapper.selectFinVourchesFileById(id);
    }

    /**
     * 查询凭证附件列表
     * 
     * @param finVourchesFile 凭证附件
     * @return 凭证附件
     */
    @Override
    public List<FinVourchesFile> selectFinVourchesFileList(FinVourchesFile finVourchesFile)
    {
        return finVourchesFileMapper.selectFinVourchesFileList(finVourchesFile);
    }

    /**
     * 新增凭证附件
     * 
     * @param finVourchesFile 凭证附件
     * @return 结果
     */
    @Override
    public int insertFinVourchesFile(FinVourchesFile finVourchesFile)
    {
        return finVourchesFileMapper.insertFinVourchesFile(finVourchesFile);
    }

    /**
     * 修改凭证附件
     * 
     * @param finVourchesFile 凭证附件
     * @return 结果
     */
    @Override
    public int updateFinVourchesFile(FinVourchesFile finVourchesFile)
    {
        return finVourchesFileMapper.updateFinVourchesFile(finVourchesFile);
    }

    /**
     * 批量删除凭证附件
     * 
     * @param ids 需要删除的凭证附件主键
     * @return 结果
     */
    @Override
    public int deleteFinVourchesFileByIds(String[] ids)
    {
        return finVourchesFileMapper.deleteFinVourchesFileByIds(ids);
    }

    /**
     * 删除凭证附件信息
     * 
     * @param id 凭证附件主键
     * @return 结果
     */
    @Override
    public int deleteFinVourchesFileById(String id)
    {
        return finVourchesFileMapper.deleteFinVourchesFileById(id);
    }

    @Override
    public int deleteFinVourchesFileByVoucherId(FinVouchers finVouchers) {
      return  finVourchesFileMapper.deleteFinVourchesFileByVoucherId(finVouchers);
    }

    @Override
    public List<FinVourchesFile> selectFinVourchesFileByVoucherId(FinVouchers finVouchers) {
                return finVourchesFileMapper.selectFinVourchesFileByVoucherId(finVouchers);
    }
}
