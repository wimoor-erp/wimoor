package com.wimoor.erp.ship.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayServicesZhihui;

public interface IShipTransCompanyZhihuiService {
	public  JSONObject shipTransDetial(UserInfo user ,String companyid, String shipmentid,String ordernum) ;

	public JSONObject shipTransDetialShipment(UserInfo user, String companyid, String shipmentid);
	
	public List<ShipTransCompayServicesZhihui> getShipTransService(UserInfo user,Integer  apiid, String accessToken) ;
	
	public 	JSONObject getCreateJson(UserInfo user , String shipmentid);
	
	public Object voidShipTransForm(UserInfo user,String shipmentid) ;
	
}
