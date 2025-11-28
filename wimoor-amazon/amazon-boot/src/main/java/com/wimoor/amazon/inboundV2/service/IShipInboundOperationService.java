package com.wimoor.amazon.inboundV2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;

public interface IShipInboundOperationService  extends IService<ShipInboundOperation>{

	ShipInboundOperation setOperationID(AmazonAuthority auth,String planid, String operationId);
	ShipInboundOperation getOperation(AmazonAuthority auth,String operationid); 
	ShipInboundOperation getOperation(AmazonAuthority auth,String fromid, String operation);
}
