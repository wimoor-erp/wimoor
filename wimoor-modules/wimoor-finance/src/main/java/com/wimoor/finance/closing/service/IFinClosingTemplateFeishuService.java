package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.FinClosingTemplateFeishu;

import java.util.List;

/**
 * 结算飞书格同步Service接口
 * 
 * @author wimoor
 * @date 2026-05-11
 */
public interface IFinClosingTemplateFeishuService 
{
    /**
     * 查询结算飞书格同步
     * 
     * @param templateid 结算飞书格同步主键
     * @return 结算飞书格同步
     */
    public FinClosingTemplateFeishu selectFinClosingTemplateFeishuByTemplateid(String templateid);

    /**
     * 查询结算飞书格同步列表
     * 
     * @param finClosingTemplateFeishu 结算飞书格同步
     * @return 结算飞书格同步集合
     */
    public List<FinClosingTemplateFeishu> selectFinClosingTemplateFeishuList(FinClosingTemplateFeishu finClosingTemplateFeishu);

    /**
     * 新增结算飞书格同步
     * 
     * @param finClosingTemplateFeishu 结算飞书格同步
     * @return 结果
     */
    public int insertFinClosingTemplateFeishu(FinClosingTemplateFeishu finClosingTemplateFeishu);

    /**
     * 修改结算飞书格同步
     * 
     * @param finClosingTemplateFeishu 结算飞书格同步
     * @return 结果
     */
    public int updateFinClosingTemplateFeishu(FinClosingTemplateFeishu finClosingTemplateFeishu);

    /**
     * 批量删除结算飞书格同步
     * 
     * @param templateids 需要删除的结算飞书格同步主键集合
     * @return 结果
     */
    public int deleteFinClosingTemplateFeishuByTemplateids(String[] templateids);

    /**
     * 删除结算飞书格同步信息
     * 
     * @param templateid 结算飞书格同步主键
     * @return 结果
     */
    public int deleteFinClosingTemplateFeishuByTemplateid(String templateid);
}
