package com.wimoor.erp.thirdparty.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyAPIMapper;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.service.IShipTransCompanyZhihuiService;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.mapper.ShipTransCompanyMapper;
import com.wimoor.erp.ship.mapper.ShipTransCompayServicesZhihuiMapper;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayServicesZhihui;
import com.wimoor.erp.util.ZmRequest;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("shipTransCompanyZhihuiService")
@RequiredArgsConstructor
public class ShipTransCompanyZhihuiServiceImpl extends ServiceImpl<ShipTransCompayServicesZhihuiMapper,ShipTransCompayServicesZhihui> implements IShipTransCompanyZhihuiService {
	final ShipTransCompanyMapper shipTransCompanyMapper;
	final ThirdPartyAPIMapper thirdPartyAPIMapper;
    final ShipTransCompayServicesZhihuiMapper shipTransCompayServicesZhihuiMapper;
	public JSONObject getApiJson(UserInfo user , ThirdPartyAPI companyapi, ShipTransCompany stcompany, String  shipmentid)  {
		String url=companyapi.getApi()+"/api/v4/shipment/info";
		JSONObject token = new JSONObject();
		token.put("access_token", companyapi.getToken());
		JSONObject shipment = new JSONObject();
		shipment.put("shipment_id","");
		shipment.put("client_reference", shipmentid);
		JSONObject json = new JSONObject();
		json.put("validation", token);
		json.put("shipment", shipment);
		String contentType = "application/json";
		String response = null;
		try {
			response = HttpClientUtil.postUrl(url,json.toString(), null);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			throw new BizException(e.getMessage());
		}
		JSONObject result = GeneralUtil.getJsonObject(response);
		result.put("ftype", "ZH");
        result.put("detail", shipTransDetialShipment(user, companyapi, shipmentid));

		if(result.getString("status").equals("0")) {
			return result;
		}else {
			JSONObject data = result.getJSONObject("data");
			JSONObject shipmentjson = data.getJSONObject("shipment");
			String service = shipmentjson.getString("service");
			String service_name=getShipTransService(user, companyapi, stcompany,service);
			shipmentjson.put("service_name", service_name);
			return result;
		}
}

	public String getShipTransService(UserInfo user, ThirdPartyAPI api, ShipTransCompany accessToken, String service) {
		// TODO Auto-generated method stub
		QueryWrapper<ShipTransCompayServicesZhihui> queryone=new QueryWrapper<ShipTransCompayServicesZhihui>();
		queryone.eq("code", service);
		queryone.eq("apiid", api.getId());
		ShipTransCompayServicesZhihui result = shipTransCompayServicesZhihuiMapper.selectOne(queryone);
		if(result!=null)return result.getName();
		String url=api.getApi()+"/api/v4/shipment/get_services";
		try {
			JSONObject token = new JSONObject();
			token.put("access_token", api.getToken());
			JSONObject servicesjson = new JSONObject();
			servicesjson.put("type", "all");
			JSONObject json = new JSONObject();
			json.put("validation", token);
			json.put("services", servicesjson);
			String contentType = "application/json";
			String response = HttpClientUtil.postUrl(url,json.toString(), null);
			if(!StrUtil.isEmpty(response)) {
				json=	GeneralUtil.getJsonObject(response);
				JSONObject data = json.getJSONObject("data");
				JSONArray services = data.getJSONArray("services");
				for(int i=0;i<services.size();i++) {
					JSONObject item = services.getJSONObject(i);
					String code = item.getString("code");
					String id = item.getString("id");
					String name = item.getString("name");
					String type = item.getString("type");
					ShipTransCompayServicesZhihui record=new ShipTransCompayServicesZhihui();
					record.setCode(code);
					record.setId(id);
					record.setFtype(type);
					record.setName(name);
					record.setApiid(api.getId());
					if(code.equals(service)) { result=record; }
					QueryWrapper<ShipTransCompayServicesZhihui> queryold=new QueryWrapper<ShipTransCompayServicesZhihui>();
					queryold.eq("id", id);
					queryold.eq("apiid", api.getId());
					ShipTransCompayServicesZhihui old = shipTransCompayServicesZhihuiMapper.selectOne(queryold);
					if(old!=null) {
						shipTransCompayServicesZhihuiMapper.update(record,queryold);
					}else {
						shipTransCompayServicesZhihuiMapper.insert(record);
					}
				}
				return result.getName();
			}else {
				return null;
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	return null;
	}

	public JSONObject shipTransDetialShipment(UserInfo user, ThirdPartyAPI companyapi, String reference) {
		// TODO Auto-generated method stub
		String url=companyapi.getApi()+"/api/v4/shipment/tracking";
		try {
			JSONObject token = new JSONObject();
			token.put("access_token", companyapi.getToken());
			JSONObject shipment = new JSONObject();
			shipment.put("client_reference", reference);
			shipment.put("parcel_number", "");
			shipment.put("language", "zh");
			JSONObject json = new JSONObject();
			json.put("validation", token);
			json.put("shipment", shipment);
			String contentType = "application/json";
			String response = HttpClientUtil.postUrl(url,json.toString(), null);
 			JSONObject result = GeneralUtil.getJsonObject(response);
			return result;
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	return null;
	}

 
}



