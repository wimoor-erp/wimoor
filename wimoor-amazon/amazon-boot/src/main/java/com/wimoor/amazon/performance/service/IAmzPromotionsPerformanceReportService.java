package com.wimoor.amazon.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.performance.pojo.dto.PerformanceReportListDTO;
import com.wimoor.amazon.performance.pojo.entity.AmzCouponPerformanceReport;
import com.wimoor.amazon.performance.pojo.entity.AmzPromotionsPerformanceReport;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
public interface IAmzPromotionsPerformanceReportService extends IService<AmzPromotionsPerformanceReport> {

    IPage<Map<String,Object>> findByCondition(PerformanceReportListDTO dto);
}
