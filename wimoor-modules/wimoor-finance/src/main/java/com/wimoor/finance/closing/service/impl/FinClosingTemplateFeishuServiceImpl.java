package com.wimoor.finance.closing.service.impl;

import com.wimoor.finance.closing.domain.FinClosingTemplateFeishu;
import com.wimoor.finance.closing.mapper.FinClosingTemplateFeishuMapper;
import com.wimoor.finance.closing.service.IFinClosingTemplateFeishuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 结算飞书格同步Service业务层处理
 * 
 * @author wimoor
 * @date 2026-05-11
 */
@Service
public class FinClosingTemplateFeishuServiceImpl implements IFinClosingTemplateFeishuService 
{
    @Autowired
    private FinClosingTemplateFeishuMapper finClosingTemplateFeishuMapper;

    /**
     * 查询结算飞书格同步
     * 
     * @param templateid 结算飞书格同步主键
     * @return 结算飞书格同步
     */
    @Override
    public FinClosingTemplateFeishu selectFinClosingTemplateFeishuByTemplateid(String templateid)
    {
        return finClosingTemplateFeishuMapper.selectFinClosingTemplateFeishuByTemplateid(templateid);
    }

    /**
     * 查询结算飞书格同步列表
     * 
     * @param finClosingTemplateFeishu 结算飞书格同步
     * @return 结算飞书格同步
     */
    @Override
    public List<FinClosingTemplateFeishu> selectFinClosingTemplateFeishuList(FinClosingTemplateFeishu finClosingTemplateFeishu)
    {
        return finClosingTemplateFeishuMapper.selectFinClosingTemplateFeishuList(finClosingTemplateFeishu);
    }

    /**
     * 新增结算飞书格同步
     * 
     * @param finClosingTemplateFeishu 结算飞书格同步
     * @return 结果
     */
    @Override
    public int insertFinClosingTemplateFeishu(FinClosingTemplateFeishu finClosingTemplateFeishu)
    {
        return finClosingTemplateFeishuMapper.insertFinClosingTemplateFeishu(finClosingTemplateFeishu);
    }

    /**
     * 修改结算飞书格同步
     * 
     * @param finClosingTemplateFeishu 结算飞书格同步
     * @return 结果
     */
    @Override
    public int updateFinClosingTemplateFeishu(FinClosingTemplateFeishu finClosingTemplateFeishu)
    {
        return finClosingTemplateFeishuMapper.updateFinClosingTemplateFeishu(finClosingTemplateFeishu);
    }

    /**
     * 批量删除结算飞书格同步
     * 
     * @param templateids 需要删除的结算飞书格同步主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateFeishuByTemplateids(String[] templateids)
    {
        return finClosingTemplateFeishuMapper.deleteFinClosingTemplateFeishuByTemplateids(templateids);
    }

    /**
     * 删除结算飞书格同步信息
     * 
     * @param templateid 结算飞书格同步主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateFeishuByTemplateid(String templateid)
    {
        return finClosingTemplateFeishuMapper.deleteFinClosingTemplateFeishuByTemplateid(templateid);
    }
}
