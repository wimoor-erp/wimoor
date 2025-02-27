package com.wimoor.amazon.performance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.inventory.pojo.vo.ProductInventoryVo;
import com.wimoor.amazon.performance.mapper.AmzOrderFeedbackMapper;
import com.wimoor.amazon.performance.pojo.dto.PerformanceReportListDTO;
import com.wimoor.amazon.performance.pojo.entity.AmzCouponAsin;
import com.wimoor.amazon.performance.pojo.entity.AmzOrderFeedback;
import com.wimoor.amazon.performance.service.IAmzOrderFeedbackService;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.common.pojo.entity.Picture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
@Service
@RequiredArgsConstructor
public class AmzOrderFeedbackServiceImpl extends ServiceImpl<AmzOrderFeedbackMapper, AmzOrderFeedback> implements IAmzOrderFeedbackService {
    public IPage<Map<String, Object>> findByCondition(PerformanceReportListDTO dto) {
        IPage<Map<String, Object>> page = this.baseMapper.findByCondition(dto.getPage(), dto);
        return page;
    }
}

