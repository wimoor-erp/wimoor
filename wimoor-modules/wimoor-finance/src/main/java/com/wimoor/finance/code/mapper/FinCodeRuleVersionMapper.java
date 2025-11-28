package com.wimoor.finance.code.mapper;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeRuleVersion;

/**
 * 编码规则版本Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface FinCodeRuleVersionMapper 
{
    /**
     * 查询编码规则版本
     * 
     * @param id 编码规则版本主键
     * @return 编码规则版本
     */
    public FinCodeRuleVersion selectFinCodeRuleVersionById(Long id);

    /**
     * 查询编码规则版本列表
     * 
     * @param finCodeRuleVersion 编码规则版本
     * @return 编码规则版本集合
     */
    public List<FinCodeRuleVersion> selectFinCodeRuleVersionList(FinCodeRuleVersion finCodeRuleVersion);

    /**
     * 新增编码规则版本
     * 
     * @param finCodeRuleVersion 编码规则版本
     * @return 结果
     */
    public int insertFinCodeRuleVersion(FinCodeRuleVersion finCodeRuleVersion);

    /**
     * 修改编码规则版本
     * 
     * @param finCodeRuleVersion 编码规则版本
     * @return 结果
     */
    public int updateFinCodeRuleVersion(FinCodeRuleVersion finCodeRuleVersion);

    /**
     * 删除编码规则版本
     * 
     * @param id 编码规则版本主键
     * @return 结果
     */
    public int deleteFinCodeRuleVersionById(Long id);

    /**
     * 批量删除编码规则版本
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinCodeRuleVersionByIds(Long[] ids);
}
