package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.entity.ProductRank;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-07-21
 */
public interface IProductRankService extends IService<ProductRank> {
	public int insert(ProductRank record);
}
