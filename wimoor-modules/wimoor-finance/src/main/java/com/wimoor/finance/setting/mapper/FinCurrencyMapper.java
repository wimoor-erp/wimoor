package com.wimoor.finance.setting.mapper;

import com.wimoor.finance.setting.domain.FinCurrency;

import java.util.List;

/**
 * currencyMapper接口
 * 
 * @author wimoor
 * @date 2026-01-15
 */
public interface FinCurrencyMapper 
{
    /**
     * 查询currency
     * 
     * @param code currency主键
     * @return currency
     */
    public FinCurrency selectFinCurrencyByCode(String code,String groupid);

    /**
     * 查询currency列表
     * 
     * @param finCurrency currency
     * @return currency集合
     */
    public List<FinCurrency> selectFinCurrencyList(FinCurrency finCurrency);

    /**
     * 新增currency
     * 
     * @param finCurrency currency
     * @return 结果
     */
    public int insertFinCurrency(FinCurrency finCurrency);

    /**
     * 修改currency
     * 
     * @param finCurrency currency
     * @return 结果
     */
    public int updateFinCurrency(FinCurrency finCurrency);

    /**
     * 删除currency
     * 
     * @param code currency主键
     * @return 结果
     */
    public int deleteFinCurrencyByCode(String code);

    /**
     * 批量删除currency
     * 
     * @param codes 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinCurrencyByCodes(String[] codes);
}
