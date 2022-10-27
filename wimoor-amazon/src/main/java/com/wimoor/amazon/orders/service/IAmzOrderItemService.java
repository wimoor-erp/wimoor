package com.wimoor.amazon.orders.service;

import com.amazon.spapi.model.orders.OrderItemsList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderItem;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;

/**
 * <p>
 * 订单main 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
public interface IAmzOrderItemService extends IService<AmzOrderItem>,IRunAmazonService {

 
	public void ordersItem(AmzOrderMain order,String token);
	public void handlerOrderItemListResponse( AmazonAuthority auth,
			AmzOrderMain order, OrderItemsList items);
	public void removeDataArchive();
	public void runTask() ;
	public void stopTask() ;
}
