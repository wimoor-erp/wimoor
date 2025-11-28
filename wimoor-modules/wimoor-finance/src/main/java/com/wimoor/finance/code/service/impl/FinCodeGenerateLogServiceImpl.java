package com.wimoor.finance.code.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.code.mapper.FinCodeGenerateLogMapper;
import com.wimoor.finance.code.domain.FinCodeGenerateLog;
import com.wimoor.finance.code.service.IFinCodeGenerateLogService;

/**
 * 编码生成记录Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@Service
public class FinCodeGenerateLogServiceImpl implements IFinCodeGenerateLogService 
{
    @Autowired
    private FinCodeGenerateLogMapper finCodeGenerateLogMapper;

    /**
     * 查询编码生成记录
     * 
     * @param id 编码生成记录主键
     * @return 编码生成记录
     */
    @Override
    public FinCodeGenerateLog selectFinCodeGenerateLogById(Long id)
    {
        return finCodeGenerateLogMapper.selectFinCodeGenerateLogById(id);
    }

    /**
     * 查询编码生成记录列表
     * 
     * @param finCodeGenerateLog 编码生成记录
     * @return 编码生成记录
     */
    @Override
    public List<FinCodeGenerateLog> selectFinCodeGenerateLogList(FinCodeGenerateLog finCodeGenerateLog)
    {
        return finCodeGenerateLogMapper.selectFinCodeGenerateLogList(finCodeGenerateLog);
    }

    /**
     * 新增编码生成记录
     * 
     * @param finCodeGenerateLog 编码生成记录
     * @return 结果
     */
    @Override
    public int insertFinCodeGenerateLog(FinCodeGenerateLog finCodeGenerateLog)
    {
        return finCodeGenerateLogMapper.insertFinCodeGenerateLog(finCodeGenerateLog);
    }

    /**
     * 修改编码生成记录
     * 
     * @param finCodeGenerateLog 编码生成记录
     * @return 结果
     */
    @Override
    public int updateFinCodeGenerateLog(FinCodeGenerateLog finCodeGenerateLog)
    {
        return finCodeGenerateLogMapper.updateFinCodeGenerateLog(finCodeGenerateLog);
    }

    /**
     * 批量删除编码生成记录
     * 
     * @param ids 需要删除的编码生成记录主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeGenerateLogByIds(Long[] ids)
    {
        return finCodeGenerateLogMapper.deleteFinCodeGenerateLogByIds(ids);
    }

    /**
     * 删除编码生成记录信息
     * 
     * @param id 编码生成记录主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeGenerateLogById(Long id)
    {
        return finCodeGenerateLogMapper.deleteFinCodeGenerateLogById(id);
    }

    @Override
    public Long findMaxSequence(String groupid, String subject, String typePrefix, int length, int i) {
        return finCodeGenerateLogMapper.findMaxSequence(groupid,subject, typePrefix, length, i);
    }

    @Override
    public boolean existsByGeneratedCode(String groupid,String generatedCode) {
        return finCodeGenerateLogMapper.existsByGeneratedCode(groupid,generatedCode) > 0;
    }

    @Override
    public List<FinCodeGenerateLog> findByRuleCodeOrderByGeneratedTimeDesc(String groupid,String ruleCode) {
        return finCodeGenerateLogMapper.findByRuleCodeOrderByGeneratedTimeDesc(groupid,ruleCode);
    }


    @Override
    public List<FinCodeGenerateLog> findAll() {
        return finCodeGenerateLogMapper.selectFinCodeGenerateLogList(new FinCodeGenerateLog());
    }
}
