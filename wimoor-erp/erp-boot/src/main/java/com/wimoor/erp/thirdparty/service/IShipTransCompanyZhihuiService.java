package com.wimoor.erp.thirdparty.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayServicesZhihui;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;

public interface IShipTransCompanyZhihuiService {
	public JSONObject getApiJson(UserInfo user , ThirdPartyAPI companyapi, ShipTransCompany stcompany, String  shipmentid)  ;

}
