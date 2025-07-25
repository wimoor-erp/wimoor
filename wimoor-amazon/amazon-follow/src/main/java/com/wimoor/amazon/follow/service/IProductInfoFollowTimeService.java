package com.wimoor.amazon.follow.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.follow.pojo.entity.ProductInfoFollowTime;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-07
 */
public interface IProductInfoFollowTimeService extends IService<ProductInfoFollowTime> {

	List<ProductInfoFollowTime> findByCondition(String shopid);

	public Integer addItem(ProductInfoFollowTime item);

	public Integer updateItem(ProductInfoFollowTime item);

	public Integer deleteItem(String id);
}
