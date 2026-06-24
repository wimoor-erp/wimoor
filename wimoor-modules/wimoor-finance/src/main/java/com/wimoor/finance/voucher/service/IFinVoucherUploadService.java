package com.wimoor.finance.voucher.service;

import java.util.List;
import com.wimoor.finance.voucher.domain.FinVoucherUpload;

/**
 * 凭证分录明细，存储凭证的每一笔分录信息Service接口
 * 
 * @author wimoor
 * @date 2025-11-28
 */
public interface IFinVoucherUploadService 
{
    /**
     * 查询凭证分录明细，存储凭证的每一笔分录信息
     * 
     * @param id 凭证分录明细，存储凭证的每一笔分录信息主键
     * @return 凭证分录明细，存储凭证的每一笔分录信息
     */
    public FinVoucherUpload selectFinVoucherUploadById(String id);

    /**
     * 查询凭证分录明细，存储凭证的每一笔分录信息列表
     * 
     * @param finVoucherUpload 凭证分录明细，存储凭证的每一笔分录信息
     * @return 凭证分录明细，存储凭证的每一笔分录信息集合
     */
    public List<FinVoucherUpload> selectFinVoucherUploadList(FinVoucherUpload finVoucherUpload);

    /**
     * 新增凭证分录明细，存储凭证的每一笔分录信息
     * 
     * @param finVoucherUpload 凭证分录明细，存储凭证的每一笔分录信息
     * @return 结果
     */
    public int insertFinVoucherUpload(FinVoucherUpload finVoucherUpload);

    /**
     * 修改凭证分录明细，存储凭证的每一笔分录信息
     * 
     * @param finVoucherUpload 凭证分录明细，存储凭证的每一笔分录信息
     * @return 结果
     */
    public int updateFinVoucherUpload(FinVoucherUpload finVoucherUpload);

    /**
     * 批量删除凭证分录明细，存储凭证的每一笔分录信息
     * 
     * @param ids 需要删除的凭证分录明细，存储凭证的每一笔分录信息主键集合
     * @return 结果
     */
    public int deleteFinVoucherUploadByIds(String[] ids);

    /**
     * 删除凭证分录明细，存储凭证的每一笔分录信息信息
     * 
     * @param id 凭证分录明细，存储凭证的每一笔分录信息主键
     * @return 结果
     */
    public int deleteFinVoucherUploadById(String id);

    void handleVoucherUpload();
}
