package com.wimoor.finance.setting.service.impl;

import com.wimoor.common.core.utils.DateUtils;
import com.wimoor.common.user.UserInfo;
import com.wimoor.finance.api.AmazonClientOneFeignManager;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.domain.PeriodStatus;
import com.wimoor.finance.setting.mapper.FinAccountingPeriodsMapper;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import com.wimoor.finance.voucher.service.util.FinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会计期间管理Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinAccountingPeriodsServiceImpl implements IFinAccountingPeriodsService 
{
    private static final Logger log = LoggerFactory.getLogger(FinAccountingPeriodsServiceImpl.class);
    @Autowired
    private FinAccountingPeriodsMapper finAccountingPeriodsMapper;
    @Autowired
    AmazonClientOneFeignManager amazonClientOneFeignManager;
    @Lazy
    @Autowired
    private IFinVouchersService finVouchersService;
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

    @Override
    public FinAccountingPeriods selectFinAccountingPeriodsByDate(String groupid,Date voucherDate) {
        //根据日期获取当前的会计期对象 如果没有 自动创建
        String period = FinUtil.getAccountingPeriod(voucherDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(voucherDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始，需要加1
        String periodName = String.format("%d年%02d月", year, month);
        FinAccountingPeriods accountingPeriods = finAccountingPeriodsMapper.selectByPeriod(groupid,period);
        
        if(accountingPeriods==null){
            // 期间不存在，自动创建
            accountingPeriods = new FinAccountingPeriods();
            accountingPeriods.setGroupid(groupid);
            accountingPeriods.setPeriodCode(period);
            accountingPeriods.setPeriodName(periodName);
            // 获取当前日期对应的会计期间编码
            String currentPeriodCode = FinUtil.getAccountingPeriod(new Date());
            if(period.compareTo(currentPeriodCode) <= 0){
                // 凭证日期小于等于当前日期，自动开启
                accountingPeriods.setPeriodStatus(PeriodStatus.OPEN.getValue());
            }else{
                // 未来期间，设为未开启
                accountingPeriods.setPeriodStatus(PeriodStatus.NOT_OPEN.getValue());
            }
            accountingPeriods.setIsCurrent(0);
            accountingPeriods.setStartDate(FinUtil.getFirstDayOfMonth(year, month));
            accountingPeriods.setEndDate(FinUtil.getLastDayOfMonth(year, month));
            accountingPeriods.setCreatedTime(new Date());
            finAccountingPeriodsMapper.insertFinAccountingPeriods(accountingPeriods);
        }
            // 期间已存在，检查状态
            if(accountingPeriods.getPeriodStatus() != null && accountingPeriods.getPeriodStatus() == 3){
                // 已结账的期间，不允许修改
                throw new IllegalArgumentException("无法修改已结账的会计期间：" + periodName);
            }
            if(accountingPeriods.getPeriodStatus() != null && accountingPeriods.getPeriodStatus() == 1){
                // 未开启状态，检查是否应该自动开启
                String currentPeriodCode = FinUtil.getAccountingPeriod(new Date());
                if(period.compareTo(currentPeriodCode) <= 0){
                    // 凭证日期小于等于当前日期，自动开启
                    accountingPeriods.setPeriodStatus(PeriodStatus.OPEN.getValue());
                    finAccountingPeriodsMapper.updateFinAccountingPeriods(accountingPeriods);
                    log.info("自动开启会计期间: {}", periodName);
                }else{
                    // 未来期间，提示未开启
                    throw new IllegalArgumentException("当前会计期间暂未开启：" + periodName);
                }
            }

        return accountingPeriods;
    }

    /**
     * 开期操作
     * @param groupid 账套ID
     * @param periodCode 期间编码
     * @return 操作结果
     */
    @Override
    public boolean openPeriod(String groupid, String periodCode) {
        FinAccountingPeriods period = finAccountingPeriodsMapper.selectByPeriod(groupid, periodCode);
        if (period == null) {
            return false;
        }
        
        // 只有未开启的期间才能开期
        if (period.getPeriodStatus() != PeriodStatus.NOT_OPEN.getValue()) {
            return false;
        }
        
        // 检查是否有后续期间已经结账
        List<FinAccountingPeriods> subsequentPeriods = finAccountingPeriodsMapper.selectSubsequentClosedPeriods(groupid, periodCode);
        if (!subsequentPeriods.isEmpty()) {
            return false;
        }
        
        // 更新期间状态为OPEN
        period.setPeriodStatus(PeriodStatus.OPEN.getValue());
        period.setUpdatedTime(new Date());
        return finAccountingPeriodsMapper.updateFinAccountingPeriods(period) > 0;
    }

    /**
     * 过账操作
     * @param groupid 账套ID
     * @param periodCode 期间编码
     * @return 操作结果
     */
    @Override
    public boolean postPeriod(String groupid, String periodCode) {
        FinAccountingPeriods period = finAccountingPeriodsMapper.selectByPeriod(groupid, periodCode);
        if (period == null) {
            return false;
        }
        
        // 只有已开启的期间才能过账
        if (period.getPeriodStatus() != PeriodStatus.OPEN.getValue()) {
            return false;
        }
        
        // 更新期间状态为CLOSED
        period.setPeriodStatus(PeriodStatus.CLOSED.getValue());
        period.setCloseTime(new Date());
        period.setUpdatedTime(new Date());
        return finAccountingPeriodsMapper.updateFinAccountingPeriods(period) > 0;
    }

    /**
     * 结账操作
     * @param groupid 账套ID
     * @param periodCode 期间编码
     * @return 操作结果
     */
    @Override
    public boolean closePeriod(String groupid, String periodCode) {
        FinAccountingPeriods period = finAccountingPeriodsMapper.selectByPeriod(groupid, periodCode);
        if (period == null) {
            return false;
        }
        
        // 已结账的期间直接返回成功
        if (Objects.equals(period.getPeriodStatus(), PeriodStatus.CLOSED.getValue())) {
            return true;
        }
        
        // 只有已开启的期间才能结账
        if (!Objects.equals(period.getPeriodStatus(), PeriodStatus.OPEN.getValue())) {
            return false;
        }
        
        // 更新期间状态为CLOSED
        period.setPeriodStatus(PeriodStatus.CLOSED.getValue());
        period.setCloseTime(new Date());
        period.setUpdatedTime(new Date());
        return finAccountingPeriodsMapper.updateFinAccountingPeriods(period) > 0;
    }

    /**
     * 反结账操作
     * @param groupid 账套ID
     * @param periodCode 期间编码
     * @return 操作结果
     */
    @Override
    public boolean reverseClosePeriod(String groupid, String periodCode) {
        FinAccountingPeriods period = finAccountingPeriodsMapper.selectByPeriod(groupid, periodCode);
        if (period == null) {
            return false;
        }
        
        // 只有已结账的期间才能反结账
        if (!Objects.equals(period.getPeriodStatus(), PeriodStatus.CLOSED.getValue())) {
            return false;
        }
        
        // 检查是否有后续期间已经结账
        List<FinAccountingPeriods> subsequentPeriods = finAccountingPeriodsMapper.selectSubsequentClosedPeriods(groupid, periodCode);
        if (!subsequentPeriods.isEmpty()) {
            return false;
        }
        
        // 更新期间状态为OPEN
        period.setPeriodStatus(PeriodStatus.OPEN.getValue());
        period.setCloseTime(null);
        period.setUpdatedTime(new Date());
        return finAccountingPeriodsMapper.updateFinAccountingPeriods(period) > 0;
    }

    @Override
    public FinAccountingPeriods getCurrentPeriod(String groupid) {
        FinAccountingPeriods currentPeriod = finAccountingPeriodsMapper.selectCurrentPeriod(groupid);
        if (currentPeriod == null) {
            // 当前期间不存在，生成当前年份的会计期间
            initAccountingPeriods(groupid);
            // 再次查询当前期间
            currentPeriod = finAccountingPeriodsMapper.selectCurrentPeriod(groupid);
            // 如果仍然没有当前期间，返回当前月份的期间
            if (currentPeriod == null) {
                String currentPeriodCode = FinUtil.getAccountingPeriod(new Date());
                currentPeriod = finAccountingPeriodsMapper.selectByPeriod(groupid, currentPeriodCode);
                // 如果仍然没有，创建一个当前月份的期间
                if (currentPeriod == null) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    String periodName = String.format("%d年%02d月", year, month);
                    Date startDate = FinUtil.getFirstDayOfMonth(year, month);
                    Date endDate = FinUtil.getLastDayOfMonth(year, month);
                    currentPeriod = new FinAccountingPeriods(currentPeriodCode, periodName, startDate, endDate);
                    currentPeriod.setGroupid(groupid);
                    currentPeriod.setPeriodStatus(PeriodStatus.OPEN.getValue());
                    currentPeriod.setIsCurrent(1);
                    finAccountingPeriodsMapper.insertFinAccountingPeriods(currentPeriod);
                } else {
                    // 如果存在但isCurrent不是1，更新为1
                    if (currentPeriod.getIsCurrent() != 1) {
                        currentPeriod.setIsCurrent(1);
                        currentPeriod.setPeriodStatus(PeriodStatus.OPEN.getValue());
                        currentPeriod.setUpdatedTime(new Date());
                        finAccountingPeriodsMapper.updateFinAccountingPeriods(currentPeriod);
                    }
                }
            }
        }
        return currentPeriod;
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
             this.fixCurrentPeriod(groupid);
        }
    }
    public void initAccountingPeriods(Integer year){
        List<Map<String,Object>> groupidlist = amazonClientOneFeignManager.getAmazonGroup();
        for(Map<String,Object> group:groupidlist){
            String groupid=group.get("id").toString();
            List<FinAccountingPeriods> periods = initAccountingPeriods(groupid,year);
            this.fixCurrentPeriod(groupid);
        }
    }

    /**
     * 生成指定年份的会计期间列表
     */
    private List<FinAccountingPeriods> initAccountingPeriods(String groupid,Integer year) {
        List<FinAccountingPeriods> periods = new ArrayList<>();
        // 遍历12个月，生成每个月的会计期间
        for (int month = 1; month <= 12; month++) {
            // 计算每个月的开始和结束日期
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            // 创建期间名称，如 "2026年01月"
            String periodName = String.format("%d年%02d月", year, month);
            String accountingPeriod = FinUtil.getAccountingPeriod(startDate);

            FinAccountingPeriods period = new FinAccountingPeriods(accountingPeriod, periodName,
                    DateUtils.toDate(startDate), DateUtils.toDate(endDate));
            period.setGroupid(groupid);
            periods.add(period);
            
            // 只负责初始化期间，不处理isCurrent和periodStatus
            // 这些值由fixCurrentPeriod方法来设置
            FinAccountingPeriods exist = finAccountingPeriodsMapper.selectByPeriod(groupid, accountingPeriod);
            if(exist == null){
                // 期间不存在，新增
                period.setPeriodStatus(PeriodStatus.NOT_OPEN.getValue());
                period.setIsCurrent(0);
                finAccountingPeriodsMapper.insertFinAccountingPeriods(period);
            }
            // 已存在的期间不做任何修改，保持原状
        }
        return periods;
    }


    public List<Map<String,Object>> getGroups(UserInfo user){
        List<Map<String, Object>> groupList = null;
            if (user.getGroups() != null && !user.getGroups().isEmpty()) {
                groupList = this.amazonClientOneFeignManager.getAmazonGroup();
                groupList = groupList.stream().filter(group -> user.getGroups().contains(group.get("id"))).collect(Collectors.toList());
            } else {
                groupList = this.amazonClientOneFeignManager.getAmazonGroup();
            }
            List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
            if(groupList!=null&&!groupList.isEmpty()){
               for(Map<String,Object> group:groupList){
                   if(Boolean.FALSE.equals(group.get("isdelete"))&&Boolean.TRUE.equals(group.get("isfinance"))){
                       result.add(group);
                   }
               }
            }
            return result;
    }

    /**
     * 修正会计期间状态
     * 逻辑：
     * 1. isCurrent=1（当前会计期间）= 最后一个已结账期间的下一个期间
     * 2. periodStatus=2（已开启）= 当前日期对应的期间及之前所有未结账的期间
     *    （开了没关就一直开着）
     * 3. periodStatus=1（未开启）= 当前日期之后的期间
     * 4. periodStatus=3（已关闭）= 已结账的期间，保持不变
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fixCurrentPeriod(String groupid) {
        // 1. 获取所有会计期间，按期间编码排序
        FinAccountingPeriods query = new FinAccountingPeriods();
        query.setGroupid(groupid);
        List<FinAccountingPeriods> allPeriods = finAccountingPeriodsMapper.selectFinAccountingPeriodsList(query);
        
        if (allPeriods == null || allPeriods.isEmpty()) {
            return;
        }
        
        // 按期间编码排序
        allPeriods.sort(Comparator.comparing(FinAccountingPeriods::getPeriodCode));
        
        // 创建期间编码到期间对象的映射
        Map<String, FinAccountingPeriods> periodMap = new HashMap<>();
        for (FinAccountingPeriods period : allPeriods) {
            periodMap.put(period.getPeriodCode(), period);
        }
        
        // 2. 确定当前会计期间
        String currentPeriodCode = null;
        
        // 查找结转损益凭证（凭证类型为"转"）
        FinVouchers transferQuery = new FinVouchers();
        transferQuery.setGroupid(groupid);
        transferQuery.setVoucherType("转");
        List<FinVouchers> transferVouchers = finVouchersService.selectFinVouchersSimpleList(transferQuery);
        
        if (transferVouchers != null && !transferVouchers.isEmpty()) {
            // 规则2：找到最后一个结转损益凭证，其日期的下一个月为当前会计期间
            transferVouchers.sort(Comparator.comparing(FinVouchers::getVoucherDate).reversed());
            FinVouchers lastTransferVoucher = transferVouchers.get(0);
            // 获取凭证日期对应的期间，然后取下一个
            String voucherPeriodCode = DateUtils.parseDateToStr("yyyyMM", lastTransferVoucher.getVoucherDate());
            currentPeriodCode = getNextPeriodCode(voucherPeriodCode);
            log.info("找到结转损益凭证，最后凭证期间: {}, 当前会计期间: {}", voucherPeriodCode, currentPeriodCode);
        } else {
            // 规则1：没有结转损益凭证，第一个凭证所在的月份为当前会计期间
            FinVouchers firstVoucherQuery = new FinVouchers();
            firstVoucherQuery.setGroupid(groupid);
            List<FinVouchers> allVouchers = finVouchersService.selectFinVouchersSimpleList(firstVoucherQuery);
            
            if (allVouchers != null && !allVouchers.isEmpty()) {
                allVouchers.sort(Comparator.comparing(FinVouchers::getVoucherDate));
                FinVouchers firstVoucher = allVouchers.get(0);
                currentPeriodCode = DateUtils.parseDateToStr("yyyyMM", firstVoucher.getVoucherDate());
                log.info("无结转损益凭证，第一个凭证日期: {}, 当前会计期间: {}", 
                         firstVoucher.getVoucherDate(), currentPeriodCode);
            } else {
                // 没有任何凭证，使用第一个期间
                currentPeriodCode = allPeriods.get(0).getPeriodCode();
                log.info("无任何凭证，使用第一个期间: {}", currentPeriodCode);
            }
        }
        
        // 确保currentPeriodCode在期间列表中
        if (!periodMap.containsKey(currentPeriodCode)) {
            log.warn("计算的当前会计期间 {} 不在期间列表中，使用第一个期间", currentPeriodCode);
            currentPeriodCode = allPeriods.get(0).getPeriodCode();
        }
        
        // 3. 确定当前日期对应的会计期间
        LocalDate today = LocalDate.now();
        String todayPeriodCode = String.format("%04d%02d", today.getYear(), today.getMonthValue());
        log.info("当前日期对应的会计期间: {}", todayPeriodCode);
        
        // 4. 更新所有会计期间的状态
        // 状态码：1=未开启，2=已开启，3=已关闭
        for (FinAccountingPeriods period : allPeriods) {
            String periodCode = period.getPeriodCode();
            
            // 设置isCurrent
            if (periodCode.equals(currentPeriodCode)) {
                period.setIsCurrent(1);
            } else {
                period.setIsCurrent(0);
            }
            
            // 设置periodStatus
            if (periodCode.compareTo(currentPeriodCode) < 0) {
                // 规则5：当前会计期间之前的会计期间 → 已关闭
                period.setPeriodStatus(PeriodStatus.CLOSED.getValue());
            } else if (periodCode.compareTo(currentPeriodCode) >= 0 && periodCode.compareTo(todayPeriodCode) <= 0) {
                // 规则3：从当前会计期间到当前日期对应的会计期间 → 已开启
                period.setPeriodStatus(PeriodStatus.OPEN.getValue());
            } else {
                // 规则4：未来的会计期间 → 未开启
                period.setPeriodStatus(PeriodStatus.NOT_OPEN.getValue());
            }
            
            finAccountingPeriodsMapper.updateFinAccountingPeriods(period);
        }
        
        log.info("会计期间修正完成，当前会计期间: {}", currentPeriodCode);
    }
    
    /**
     * 根据日期查找对应的会计期间
     */
    private FinAccountingPeriods findPeriodByDate(List<FinAccountingPeriods> periods, Date date) {
        if (date == null || periods == null) return null;
        
        for (FinAccountingPeriods period : periods) {
            if (period.getStartDate() != null && period.getEndDate() != null) {
                if (!date.before(period.getStartDate()) && !date.after(period.getEndDate())) {
                    return period;
                }
            }
        }
        return null;
    }
    
    /**
     * 获取下一个会计期间编码
     */
    private String getNextPeriodCode(String periodCode) {
        if (periodCode == null || periodCode.length() != 6) return null;
        
        try {
            int year = Integer.parseInt(periodCode.substring(0, 4));
            int month = Integer.parseInt(periodCode.substring(4, 6));
            
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
            
            return String.format("%04d%02d", year, month);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
