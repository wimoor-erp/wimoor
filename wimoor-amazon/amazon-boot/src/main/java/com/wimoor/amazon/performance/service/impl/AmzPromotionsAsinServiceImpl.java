package com.wimoor.amazon.performance.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.performance.mapper.AmzPromotionsAsinMapper;
import com.wimoor.amazon.performance.pojo.entity.AmzPromotionsAsin;
import com.wimoor.amazon.performance.service.IAmzPromotionsAsinService;
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
public class AmzPromotionsAsinServiceImpl extends ServiceImpl<AmzPromotionsAsinMapper, AmzPromotionsAsin> implements IAmzPromotionsAsinService {

}
