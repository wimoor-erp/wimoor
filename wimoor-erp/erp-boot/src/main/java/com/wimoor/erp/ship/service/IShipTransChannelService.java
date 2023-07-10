package com.wimoor.erp.ship.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.ship.pojo.entity.ShipTransChannel;

public interface IShipTransChannelService extends IService<ShipTransChannel>{

	List<ShipTransChannel> selectByshopid(String shopid,String name);

}
