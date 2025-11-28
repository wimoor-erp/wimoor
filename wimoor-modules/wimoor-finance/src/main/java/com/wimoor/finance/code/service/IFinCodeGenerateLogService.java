package com.wimoor.finance.code.service;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeGenerateLog;

/**
 * 编码生成记录Service接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface IFinCodeGenerateLogService 
{
    /**
     * 查询编码生成记录
     * 
     * @param id 编码生成记录主键
     * @return 编码生成记录
     */
    public FinCodeGenerateLog selectFinCodeGenerateLogById(Long id);

    /**
     * 查询编码生成记录列表
     * 
     * @param finCodeGenerateLog 编码生成记录
     * @return 编码生成记录集合
     */
    public List<FinCodeGenerateLog> selectFinCodeGenerateLogList(FinCodeGenerateLog finCodeGenerateLog);

    /**
     * 新增编码生成记录
     * 
     * @param finCodeGenerateLog 编码生成记录
     * @return 结果
     */
    public int insertFinCodeGenerateLog(FinCodeGenerateLog finCodeGenerateLog);

    /**
     * 修改编码生成记录
     * 
     * @param finCodeGenerateLog 编码生成记录
     * @return 结果
     */
    public int updateFinCodeGenerateLog(FinCodeGenerateLog finCodeGenerateLog);

    /**
     * 批量删除编码生成记录
     * 
     * @param ids 需要删除的编码生成记录主键集合
     * @return 结果
     */
    public int deleteFinCodeGenerateLogByIds(Long[] ids);

    /**
     * 删除编码生成记录信息
     * 
     * @param id 编码生成记录主键
     * @return 结果
     */
    public int deleteFinCodeGenerateLogById(Long id);

    Long findMaxSequence(String groupid, String subject, String typePrefix, int length, int i);

    boolean existsByGeneratedCode(String groupid,String generatedCode);

    List<FinCodeGenerateLog> findByRuleCodeOrderByGeneratedTimeDesc(String groupid,String ruleCode);

    List<FinCodeGenerateLog> findAll();
}
