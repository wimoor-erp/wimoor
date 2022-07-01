package com.wimoor.amazon.finances.service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.finances.pojo.entity.OrdersFinancial;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
public interface IOrdersFinancialService extends IService<OrdersFinancial> {
	List<OrdersFinancial> getOrdersFinancialList(AmazonAuthority amazonAuthority,String orderid);
}
