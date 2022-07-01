package com.wimoor.amazon.product.service.impl;

import com.wimoor.amazon.product.pojo.entity.ProductInOrder;
import com.wimoor.amazon.product.mapper.ProductInOrderMapper;
import com.wimoor.amazon.product.service.IProductInOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品信息的订单销售数据 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Service
public class ProductInOrderServiceImpl extends ServiceImpl<ProductInOrderMapper, ProductInOrder> implements IProductInOrderService {

}
