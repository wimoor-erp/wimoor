package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.entity.ProductInOrder;
import com.wimoor.common.user.UserInfo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 产品信息的订单销售数据 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
public interface IProductInOrderService extends IService<ProductInOrder> {
	Map<String, Object> selectDetialById(String pid,String shopid);

	List<Map<String, Object>> getChartData(Map<String, Integer> ftypeset, Map<String, Object> parameter, UserInfo user);
}
