package com.wimoor.finance.setting.mapper;

import java.util.List;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import org.apache.ibatis.annotations.Param;

/**
 * 会计期间管理Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface FinAccountingPeriodsMapper 
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
     * 删除会计期间管理
     * 
     * @param periodId 会计期间管理主键
     * @return 结果
     */
    public int deleteFinAccountingPeriodsByPeriodId(Long periodId);

    /**
     * 批量删除会计期间管理
     * 
     * @param periodIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinAccountingPeriodsByPeriodIds(Long[] periodIds);

    FinAccountingPeriods selectByPeriod(@Param("groupid") String groupid,
                                       @Param("period") String period);

    int updatePeriodStatus(@Param("groupid") String groupid,
                           @Param("period") String period,
                           @Param("periodStatus") Integer periodStatus,
                           @Param("closedBy") String closedBy);

    List<FinAccountingPeriods> selectOpenPeriods(@Param("groupid") String groupid);

    List<FinAccountingPeriods> selectSubsequentClosedPeriods(@Param("groupid") String groupid, @Param("period") String period);
}
