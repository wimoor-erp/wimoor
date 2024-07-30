package com.wimoor.amazon.orders.service;


import com.amazon.spapi.model.orders.GetOrdersResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmzAuthApiTimelimit;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderBuyerShipAddress;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;

/**
 * <p>
 * 订单main 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
public interface IAmzOrderMainService extends IService<AmzOrderMain>,IRunAmazonService {

	
	public int saveAddress(AmzOrderBuyerShipAddress entity);
	public void handlerOrderResponse(AmazonAuthority auth,AmzAuthApiTimelimit apilimit,GetOrdersResponse response) ;
	public void removeDataArchive();
 
}
