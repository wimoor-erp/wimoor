package com.wimoor.finance.setting.service;

import java.util.List;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;

/**
 * 会计期间管理Service接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface IFinAccountingPeriodsService 
{
    /**
     * 查询会计期间管理
     * 
     * @param periodId 会计期间管理主键
     * @return 会计期间管理
     */
    public FinAccountingPeriods selectFinAccountingPeriodsByPeriodId(Long periodId);

    /**
     * 查询会计期间管理列表
     * 
     * @param finAccountingPeriods 会计期间管理
     * @return 会计期间管理集合
     */
    public List<FinAccountingPeriods> selectFinAccountingPeriodsList(FinAccountingPeriods finAccountingPeriods);

    /**
     * 新增会计期间管理
     * 
     * @param finAccountingPeriods 会计期间管理
     * @return 结果
     */
    public int insertFinAccountingPeriods(FinAccountingPeriods finAccountingPeriods);

    /**
     * 修改会计期间管理
     * 
     * @param finAccountingPeriods 会计期间管理
     * @return 结果
     */
    public int updateFinAccountingPeriods(FinAccountingPeriods finAccountingPeriods);

    /**
     * 批量删除会计期间管理
     * 
     * @param periodIds 需要删除的会计期间管理主键集合
     * @return 结果
     */
    public int deleteFinAccountingPeriodsByPeriodIds(Long[] periodIds);

    /**
     * 删除会计期间管理信息
     * 
     * @param periodId 会计期间管理主键
     * @return 结果
     */
    public int deleteFinAccountingPeriodsByPeriodId(Long periodId);

    /**
     * 初始化会计期间管理
     *
     * @param groupId 会计期间管理主键
     * @return 结果
     */
    void initAccountingPeriods(String groupId);

     /**
     * 初始化会计期间管理
     *
     * @param year 会计期间管理主键
     * @return 结果
     */
    void initAccountingPeriods(Integer year);
    /**
     * 初始化会计期间管理
     *
     * @return 结果
     */
    void initAccountingPeriods();

    FinAccountingPeriods selectByPeriod(String groupid, String period);
    /**
     * 查询后续已关闭的会计期间
     *
     * @param groupid 集团ID
     * @param period 会计期间
     * @return 后续已关闭的会计期间列表
     */
    List<FinAccountingPeriods> selectSubsequentClosedPeriods(String groupid, String period);
}
