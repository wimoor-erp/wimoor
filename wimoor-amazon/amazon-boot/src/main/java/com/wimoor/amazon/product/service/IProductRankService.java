package com.wimoor.amazon.product.service;
import com.wimoor.amazon.product.pojo.entity.ProductRank;

import java.util.List;
import java.util.Map;

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

	public List<Map<String, Object>> findProductRank(String id);
}
