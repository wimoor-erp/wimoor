package com.wimoor.erp.ship.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.ship.pojo.entity.ShipTransDetail;

public interface IShipTransDetailService extends IService<ShipTransDetail> {

	int usedTransType(String shopid, String transtype);

}
