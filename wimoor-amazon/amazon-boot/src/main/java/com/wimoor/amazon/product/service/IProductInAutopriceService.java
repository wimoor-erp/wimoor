package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.entity.FollowOfferChange;
import com.wimoor.amazon.product.pojo.entity.ProductInAutoprice;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-20
 */
public interface IProductInAutopriceService extends IService<ProductInAutoprice> {
	void changeTaskPriceSku(String asin, String marketplaceid, Map<String, FollowOfferChange> mapseller);
}
