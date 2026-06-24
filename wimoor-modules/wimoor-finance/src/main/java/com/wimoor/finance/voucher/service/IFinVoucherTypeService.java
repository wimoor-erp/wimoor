package com.wimoor.finance.voucher.service;

import com.wimoor.finance.voucher.domain.FinVoucherType;

import java.util.List;

/**
 * 凭证字Service接口
 * 
 * @author wimoor
 * @date 2026-06-08
 */
public interface IFinVoucherTypeService 
{
    /**
     * 查询凭证字
     * 
     * @param id 凭证字主键
     * @return 凭证字
     */
    public FinVoucherType selectFinVoucherTypeById(String id);

    /**
     * 查询凭证字列表
     * 
     * @param finVoucherType 凭证字
     * @return 凭证字集合
     */
    public List<FinVoucherType> selectFinVoucherTypeList(FinVoucherType finVoucherType);

    /**
     * 新增凭证字
     * 
     * @param finVoucherType 凭证字
     * @return 结果
     */
    public int insertFinVoucherType(FinVoucherType finVoucherType);

    /**
     * 修改凭证字
     * 
     * @param finVoucherType 凭证字
     * @return 结果
     */
    public int updateFinVoucherType(FinVoucherType finVoucherType);

    /**
     * 批量删除凭证字
     * 
     * @param ids 需要删除的凭证字主键集合
     * @return 结果
     */
    public int deleteFinVoucherTypeByIds(String[] ids);

    /**
     * 删除凭证字信息
     * 
     * @param id 凭证字主键
     * @return 结果
     */
    public int deleteFinVoucherTypeById(String id);
}
