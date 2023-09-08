package com.wimoor.amazon.inbound.service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.FBAShipCycle;
import com.wimoor.common.user.UserInfo;

public interface IFBAShipCycleService extends IService<FBAShipCycle>{

	public int updateStockCycle(String groupid,String marketplaceid,String sku,Integer stockcycle,Integer mincycle,BigDecimal fee, UserInfo user);

	public boolean updateMinCycle(String groupid,String marketplaceid,String type,String sku,Integer num, UserInfo user) ;

	public boolean updateFirstLegCharges(String groupid,String marketplaceid,String type,String sku,Integer num, UserInfo user);

	public FBAShipCycle getFbaShipCycle(String groupid,String marketplaceid,String sku);
	
	public Map<String,FBAShipCycle> getFbaShipCycle(String groupid,String marketplaceid);
	public int updateStockCycleTransType(FBAShipCycle cycle, UserInfo user) ;

}
