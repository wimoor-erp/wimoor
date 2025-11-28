package com.wimoor.finance.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.code.mapper.FinCodeRuleVersionMapper;
import com.wimoor.finance.code.domain.FinCodeRuleVersion;
import com.wimoor.finance.code.service.IFinCodeRuleVersionService;

/**
 * 编码规则版本Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@Service
public class FinCodeRuleVersionServiceImpl implements IFinCodeRuleVersionService 
{
    @Autowired
    private FinCodeRuleVersionMapper finCodeRuleVersionMapper;

    /**
     * 查询编码规则版本
     * 
     * @param id 编码规则版本主键
     * @return 编码规则版本
     */
    @Override
    public FinCodeRuleVersion selectFinCodeRuleVersionById(Long id)
    {
        return finCodeRuleVersionMapper.selectFinCodeRuleVersionById(id);
    }

    /**
     * 查询编码规则版本列表
     * 
     * @param finCodeRuleVersion 编码规则版本
     * @return 编码规则版本
     */
    @Override
    public List<FinCodeRuleVersion> selectFinCodeRuleVersionList(FinCodeRuleVersion finCodeRuleVersion)
    {
        return finCodeRuleVersionMapper.selectFinCodeRuleVersionList(finCodeRuleVersion);
    }

    /**
     * 新增编码规则版本
     * 
     * @param finCodeRuleVersion 编码规则版本
     * @return 结果
     */
    @Override
    public int insertFinCodeRuleVersion(FinCodeRuleVersion finCodeRuleVersion)
    {
        return finCodeRuleVersionMapper.insertFinCodeRuleVersion(finCodeRuleVersion);
    }

    /**
     * 修改编码规则版本
     * 
     * @param finCodeRuleVersion 编码规则版本
     * @return 结果
     */
    @Override
    public int updateFinCodeRuleVersion(FinCodeRuleVersion finCodeRuleVersion)
    {
        return finCodeRuleVersionMapper.updateFinCodeRuleVersion(finCodeRuleVersion);
    }

    /**
     * 批量删除编码规则版本
     * 
     * @param ids 需要删除的编码规则版本主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleVersionByIds(Long[] ids)
    {
        return finCodeRuleVersionMapper.deleteFinCodeRuleVersionByIds(ids);
    }

    /**
     * 删除编码规则版本信息
     * 
     * @param id 编码规则版本主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleVersionById(Long id)
    {
        return finCodeRuleVersionMapper.deleteFinCodeRuleVersionById(id);
    }
}
