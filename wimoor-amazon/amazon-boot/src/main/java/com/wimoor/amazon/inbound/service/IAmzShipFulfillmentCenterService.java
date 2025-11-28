package com.wimoor.amazon.inbound.service;

import com.wimoor.amazon.inbound.pojo.entity.AmzShipFulfillmentCenter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-12-29
 */
public interface IAmzShipFulfillmentCenterService extends IService<AmzShipFulfillmentCenter> {

	AmzShipFulfillmentCenter getByCode(String destinationfulfillmentcenterid);

}
