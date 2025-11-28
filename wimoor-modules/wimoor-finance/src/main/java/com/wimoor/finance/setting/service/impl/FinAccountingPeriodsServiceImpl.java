package com.wimoor.finance.setting.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.wimoor.common.core.utils.DateUtils;
import com.wimoor.finance.api.AmazonClientOneFeignManager;
import com.wimoor.finance.setting.domain.PeriodStatus;
import com.wimoor.finance.voucher.service.util.FinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.setting.mapper.FinAccountingPeriodsMapper;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;

/**
 * 会计期间管理Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinAccountingPeriodsServiceImpl implements IFinAccountingPeriodsService 
{
    @Autowired
    private FinAccountingPeriodsMapper finAccountingPeriodsMapper;
    @Autowired
    AmazonClientOneFeignManager amazonClientOneFeignManager;
    /**
     * 查询会计期间管理
     * 
     * @param periodId 会计期间管理主键
     * @return 会计期间管理
     */
    @Override
    public FinAccountingPeriods selectFinAccountingPeriodsByPeriodId(Long periodId)
    {
        return finAccountingPeriodsMapper.selectFinAccountingPeriodsByPeriodId(periodId);
    }

    /**
     * 查询会计期间管理列表
     * 
     * @param finAccountingPeriods 会计期间管理
     * @return 会计期间管理
     */
    @Override
    public List<FinAccountingPeriods> selectFinAccountingPeriodsList(FinAccountingPeriods finAccountingPeriods)
    {
        return finAccountingPeriodsMapper.selectFinAccountingPeriodsList(finAccountingPeriods);
    }

    /**
     * 新增会计期间管理
     * 
     * @param finAccountingPeriods 会计期间管理
     * @return 结果
     */
    @Override
    public int insertFinAccountingPeriods(FinAccountingPeriods finAccountingPeriods)
    {
        return finAccountingPeriodsMapper.insertFinAccountingPeriods(finAccountingPeriods);
    }

    /**
     * 修改会计期间管理
     * 
     * @param finAccountingPeriods 会计期间管理
     * @return 结果
     */
    @Override
    public int updateFinAccountingPeriods(FinAccountingPeriods finAccountingPeriods)
    {
        return finAccountingPeriodsMapper.updateFinAccountingPeriods(finAccountingPeriods);
    }

    /**
     * 批量删除会计期间管理
     * 
     * @param periodIds 需要删除的会计期间管理主键
     * @return 结果
     */
    @Override
    public int deleteFinAccountingPeriodsByPeriodIds(Long[] periodIds)
    {
        return finAccountingPeriodsMapper.deleteFinAccountingPeriodsByPeriodIds(periodIds);
    }

    /**
     * 删除会计期间管理信息
     * 
     * @param periodId 会计期间管理主键
     * @return 结果
     */
    @Override
    public int deleteFinAccountingPeriodsByPeriodId(Long periodId)
    {
        return finAccountingPeriodsMapper.deleteFinAccountingPeriodsByPeriodId(periodId);
    }
    public void initAccountingPeriods(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        initAccountingPeriods(currentYear);
        int currentMonth = calendar.get(Calendar.MONTH);
        if(currentMonth==11){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            currentYear=calendar.get(Calendar.YEAR);
            initAccountingPeriods(currentYear);
        }
    }

    @Override
    public FinAccountingPeriods selectByPeriod(String groupid, String period) {
        return this.finAccountingPeriodsMapper.selectByPeriod(groupid,period);
    }

    @Override
    public List<FinAccountingPeriods> selectSubsequentClosedPeriods(String groupid, String period) {
        return this.finAccountingPeriodsMapper.selectSubsequentClosedPeriods(groupid, period);
    }

    public void initAccountingPeriods(String  groupid){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        List<FinAccountingPeriods> periods = initAccountingPeriods(groupid,currentYear);
        int currentMonth = calendar.get(Calendar.MONTH);
        if(currentMonth==11){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            currentYear=calendar.get(Calendar.YEAR);
             initAccountingPeriods(groupid,currentYear);
        }
    }
    public void initAccountingPeriods(Integer year){
        List<Map<String,Object>> groupidlist = amazonClientOneFeignManager.getAmazonGroup();
        for(Map<String,Object> group:groupidlist){
            String groupid=group.get("id").toString();
            List<FinAccountingPeriods> periods = initAccountingPeriods(groupid,year);
        }
    }

    /**
     * 生成指定年份的会计期间列表
     */
    private List<FinAccountingPeriods> initAccountingPeriods(String groupid,Integer year) {
        List<FinAccountingPeriods> periods = new ArrayList<>();
        //获取当前月
        int currentMonth = LocalDate.now().getMonthValue();
        // 遍历12个月，生成每个月的会计期间
        for (int month = 1; month <= 12; month++) {
            // 计算每个月的开始和结束日期
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            // 创建期间名称，如 "2024-01"
            String periodName = String.format("%d年%02d月", year, month);
             String accountingPeriod = FinUtil.getAccountingPeriod(startDate);

            FinAccountingPeriods period = new FinAccountingPeriods(accountingPeriod, periodName,
                    DateUtils.toDate(startDate), DateUtils.toDate(endDate) );
            period.setGroupid(groupid);
            periods.add(period);
            FinAccountingPeriods exist = finAccountingPeriodsMapper.selectByPeriod(groupid,accountingPeriod);
            if(exist==null){
                if(currentMonth==month&&period.getIsCurrent()==0){
                    period.setPeriodStatus(PeriodStatus.OPEN.getValue());
                    period.setIsCurrent(1);
                }else {
                    period.setIsCurrent(0);
                }
                finAccountingPeriodsMapper.insertFinAccountingPeriods(period);
            }else if(exist.getPeriodStatus()!=3){
                if(currentMonth==month&&exist.getIsCurrent()==0){
                    exist.setIsCurrent(1);
                    exist.setPeriodStatus(PeriodStatus.OPEN.getValue());
                }else {
                    exist.setIsCurrent(0);
                }
                finAccountingPeriodsMapper.updateFinAccountingPeriods(exist);
            }

        }

        // 可选：添加第13期间（调整期间）
        // periods.add(createAdjustmentPeriod(fiscalYear));

        return periods;
    }

}
