package com.wimoor.amazon.inboundV2.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCase;

public interface IShipInboundCaseService  extends IService<ShipInboundCase>{

	List<ShipInboundCase> findByBox(String boxid, Integer boxnum);

	List<ShipInboundCase> findShipInboundCaseByBoxid(String boxid);

}
