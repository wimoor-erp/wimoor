package com.wimoor.finance.code.mapper;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeGenerateLog;
import org.apache.ibatis.annotations.Param;

/**
 * 编码生成记录Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface FinCodeGenerateLogMapper 
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
     * 删除编码生成记录
     * 
     * @param id 编码生成记录主键
     * @return 结果
     */
    public int deleteFinCodeGenerateLogById(Long id);

    /**
     * 批量删除编码生成记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinCodeGenerateLogByIds(Long[] ids);

    /**
     * 查询最大序列值
     *
     * @param groupid 租户ID
     * @param subject 主题
     * @param typePrefix 类型前缀
     * @param length 编码长度
     * @param i 序列长度
     * @return 最大序列值
     */
    Long findMaxSequence(@Param("groupid") String groupid,
                         @Param("subject") String subject,
                         @Param("typePrefix") String typePrefix,
                         @Param("length") int length,
                         @Param("i") int i);

    /**
     * 检查编码是否存在
     *
     * @param groupid 租户ID
     * @param generatedCode 生成的编码
     * @return 存在返回1，不存在返回0
     */
    int existsByGeneratedCode(@Param("groupid") String groupid,
                              @Param("generatedCode") String generatedCode);

    List<FinCodeGenerateLog> findByRuleCodeOrderByGeneratedTimeDesc(@Param("groupid") String groupid,
                                                                     @Param("ruleCode") String ruleCode);

}
