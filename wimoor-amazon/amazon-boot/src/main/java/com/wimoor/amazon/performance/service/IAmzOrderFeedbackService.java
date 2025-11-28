package com.wimoor.amazon.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.performance.pojo.dto.PerformanceReportListDTO;
import com.wimoor.amazon.performance.pojo.entity.AmzOrderFeedback;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
public interface IAmzOrderFeedbackService extends IService<AmzOrderFeedback> {
    IPage<Map<String,Object>> findByCondition(PerformanceReportListDTO dto);
}
