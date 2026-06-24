package com.wimoor.finance.setting.service.impl;

import java.util.List;

import com.wimoor.finance.setting.domain.FinCurrency;
import com.wimoor.finance.setting.mapper.FinCurrencyMapper;
import com.wimoor.finance.setting.service.IFinCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * currencyService业务层处理
 * 
 * @author wimoor
 * @date 2026-01-15
 */
@Service
public class FinCurrencyServiceImpl implements IFinCurrencyService
{
    @Autowired
    private FinCurrencyMapper finCurrencyMapper;

    /**
     * 查询currency
     * 
     * @param code currency主键
     * @return currency
     */
    @Override
    public FinCurrency selectFinCurrencyByCode(String code,String groupid)
    {
        return finCurrencyMapper.selectFinCurrencyByCode(code,groupid);
    }

    /**
     * 查询currency列表
     * 
     * @param finCurrency currency
     * @return currency
     */
    @Override
    public List<FinCurrency> selectFinCurrencyList(FinCurrency finCurrency)
    {
        return finCurrencyMapper.selectFinCurrencyList(finCurrency);
    }

    /**
     * 新增currency
     * 
     * @param finCurrency currency
     * @return 结果
     */
    @Override
    public int insertFinCurrency(FinCurrency finCurrency)
    {
        return finCurrencyMapper.insertFinCurrency(finCurrency);
    }

    /**
     * 修改currency
     * 
     * @param finCurrency currency
     * @return 结果
     */
    @Override
    public int updateFinCurrency(FinCurrency finCurrency)
    {
        return finCurrencyMapper.updateFinCurrency(finCurrency);
    }

    /**
     * 批量删除currency
     * 
     * @param codes 需要删除的currency主键
     * @return 结果
     */
    @Override
    public int deleteFinCurrencyByCodes(String[] codes)
    {
        return finCurrencyMapper.deleteFinCurrencyByCodes(codes);
    }

    /**
     * 删除currency信息
     * 
     * @param code currency主键
     * @return 结果
     */
    @Override
    public int deleteFinCurrencyByCode(String code)
    {
        return finCurrencyMapper.deleteFinCurrencyByCode(code);
    }
}
