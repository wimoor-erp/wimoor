package com.wimoor.finance.voucher.service.impl;

import com.wimoor.finance.voucher.domain.FinVoucherType;
import com.wimoor.finance.voucher.mapper.FinVoucherTypeMapper;
import com.wimoor.finance.voucher.service.IFinVoucherTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 凭证字Service业务层处理
 * 
 * @author wimoor
 * @date 2026-06-08
 */
@Service
public class FinVoucherTypeServiceImpl implements IFinVoucherTypeService 
{
    @Autowired
    private FinVoucherTypeMapper finVoucherTypeMapper;

    /**
     * 查询凭证字
     * 
     * @param id 凭证字主键
     * @return 凭证字
     */
    @Override
    public FinVoucherType selectFinVoucherTypeById(String id)
    {
        return finVoucherTypeMapper.selectFinVoucherTypeById(id);
    }

    /**
     * 查询凭证字列表
     * 
     * @param finVoucherType 凭证字
     * @return 凭证字
     */
    @Override
    public List<FinVoucherType> selectFinVoucherTypeList(FinVoucherType finVoucherType)
    {
        return finVoucherTypeMapper.selectFinVoucherTypeList(finVoucherType);
    }

    /**
     * 新增凭证字
     * 
     * @param finVoucherType 凭证字
     * @return 结果
     */
    @Override
    public int insertFinVoucherType(FinVoucherType finVoucherType)
    {
        finVoucherType.setOpttime(new Date());
        return finVoucherTypeMapper.insertFinVoucherType(finVoucherType);
    }

    /**
     * 修改凭证字
     * 
     * @param finVoucherType 凭证字
     * @return 结果
     */
    @Override
    public int updateFinVoucherType(FinVoucherType finVoucherType)
    {
        finVoucherType.setOpttime(new Date());
        return finVoucherTypeMapper.updateFinVoucherType(finVoucherType);
    }

    /**
     * 批量删除凭证字
     * 
     * @param ids 需要删除的凭证字主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherTypeByIds(String[] ids)
    {
        return finVoucherTypeMapper.deleteFinVoucherTypeByIds(ids);
    }

    /**
     * 删除凭证字信息
     * 
     * @param id 凭证字主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherTypeById(String id)
    {
        return finVoucherTypeMapper.deleteFinVoucherTypeById(id);
    }
}
