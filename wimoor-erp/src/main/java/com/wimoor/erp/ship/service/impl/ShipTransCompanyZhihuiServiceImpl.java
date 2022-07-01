package com.wimoor.erp.ship.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.mapper.ShipTransCompayServicesZhihuiMapper;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayServicesZhihui;
import com.wimoor.erp.ship.service.IShipTransCompanyZhihuiService;

import lombok.RequiredArgsConstructor;

@Service("shipTransCompanyZhihuiService")
@RequiredArgsConstructor
public class ShipTransCompanyZhihuiServiceImpl extends ServiceImpl<ShipTransCompayServicesZhihuiMapper,ShipTransCompayServicesZhihui> implements IShipTransCompanyZhihuiService {@Override
	public Object shipTransDetial(UserInfo user, String companyid, String shipmentid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object shipTransDetialShipment(UserInfo user, String companyid, String shipmentid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ShipTransCompayServicesZhihui> getShipTransService(UserInfo user, Integer apiid, String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getCreateJson(UserInfo user, String shipmentid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object voidShipTransForm(UserInfo user, String shipmentid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object submitShipTransForm(UserInfo user, String shipmentid, String jsonstr) {
		// TODO Auto-generated method stub
		return null;
	}

}



