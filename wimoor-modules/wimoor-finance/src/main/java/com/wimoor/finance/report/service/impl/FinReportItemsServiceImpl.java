package com.wimoor.finance.report.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.report.mapper.FinReportItemsMapper;
import com.wimoor.finance.report.domain.FinReportItems;
import com.wimoor.finance.report.service.IFinReportItemsService;

/**
 * 报表项目Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinReportItemsServiceImpl implements IFinReportItemsService 
{
    @Autowired
    private FinReportItemsMapper finReportItemsMapper;

    /**
     * 查询报表项目
     * 
     * @param itemId 报表项目主键
     * @return 报表项目
     */
    @Override
    public FinReportItems selectFinReportItemsByItemId(Long itemId)
    {
        return finReportItemsMapper.selectFinReportItemsByItemId(itemId);
    }

    /**
     * 查询报表项目列表
     * 
     * @param finReportItems 报表项目
     * @return 报表项目
     */
    @Override
    public List<FinReportItems> selectFinReportItemsList(FinReportItems finReportItems)
    {
        //帮我reportItems进行排序按lineNumber
        List<FinReportItems> reportItems = finReportItemsMapper.selectFinReportItemsList(finReportItems);
        //先按level升序排序，再按linenumber升序排序
        reportItems.sort((item1, item2) -> {
            // 先按level比较
             if(item1.getItemLevel()!=null&&item2.getItemLevel()!=null){
                 if(item1.getItemLevel().equals(item2.getItemLevel())&&item1.getItemLevel()==3){
                     // 如果level相同，则按linenumber比较
                     int line1=item1.getLineNumber()!=null?item1.getLineNumber():0;
                     int line2=item2.getLineNumber()!=null?item2.getLineNumber():0;
                     return Integer.compare(line1, line2);
                 }
             }
            return item1.getItemId()!=null?item1.getItemId().compareTo(item2.getItemId()):0;

        });

        return reportItems;
    }

    /**
     * 新增报表项目
     * 
     * @param finReportItems 报表项目
     * @return 结果
     */
    @Override
    public int insertFinReportItems(FinReportItems finReportItems)
    {
        return finReportItemsMapper.insertFinReportItems(finReportItems);
    }

    /**
     * 修改报表项目
     * 
     * @param finReportItems 报表项目
     * @return 结果
     */
    @Override
    public int updateFinReportItems(FinReportItems finReportItems)
    {
        return finReportItemsMapper.updateFinReportItems(finReportItems);
    }

    /**
     * 批量删除报表项目
     * 
     * @param itemIds 需要删除的报表项目主键
     * @return 结果
     */
    @Override
    public int deleteFinReportItemsByItemIds(Long[] itemIds)
    {
        return finReportItemsMapper.deleteFinReportItemsByItemIds(itemIds);
    }

    /**
     * 删除报表项目信息
     * 
     * @param itemId 报表项目主键
     * @return 结果
     */
    @Override
    public int deleteFinReportItemsByItemId(Long itemId)
    {
        return finReportItemsMapper.deleteFinReportItemsByItemId(itemId);
    }
}
