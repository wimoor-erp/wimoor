package com.wimoor.amazon.performance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.performance.mapper.AmzCouponAsinMapper;
import com.wimoor.amazon.performance.pojo.entity.AmzCouponAsin;
import com.wimoor.amazon.performance.service.IAmzCouponAsinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class AmzCouponAsinServiceImpl extends ServiceImpl<AmzCouponAsinMapper, AmzCouponAsin> implements IAmzCouponAsinService {

}
