package com.wimoor.erp.thirdparty.service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayServicesZhihui;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;

import java.util.List;

public interface IShipTransCompanyZMService {
	public JSONObject getApiJson(UserInfo user , ThirdPartyAPI companyapi, ShipTransCompany stcompany, String ordernum);
}
