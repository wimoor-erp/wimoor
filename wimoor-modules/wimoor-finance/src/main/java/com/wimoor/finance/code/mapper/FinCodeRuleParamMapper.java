package com.wimoor.finance.code.mapper;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeRuleParam;

/**
 * 编码规则参数Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface FinCodeRuleParamMapper 
{
    /**
     * 查询编码规则参数
     * 
     * @param id 编码规则参数主键
     * @return 编码规则参数
     */
    public FinCodeRuleParam selectFinCodeRuleParamById(Long id);

    /**
     * 查询编码规则参数列表
     * 
     * @param finCodeRuleParam 编码规则参数
     * @return 编码规则参数集合
     */
    public List<FinCodeRuleParam> selectFinCodeRuleParamList(FinCodeRuleParam finCodeRuleParam);

    /**
     * 新增编码规则参数
     * 
     * @param finCodeRuleParam 编码规则参数
     * @return 结果
     */
    public int insertFinCodeRuleParam(FinCodeRuleParam finCodeRuleParam);

    /**
     * 修改编码规则参数
     * 
     * @param finCodeRuleParam 编码规则参数
     * @return 结果
     */
    public int updateFinCodeRuleParam(FinCodeRuleParam finCodeRuleParam);

    /**
     * 删除编码规则参数
     * 
     * @param id 编码规则参数主键
     * @return 结果
     */
    public int deleteFinCodeRuleParamById(Long id);

    /**
     * 批量删除编码规则参数
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinCodeRuleParamByIds(Long[] ids);
}
