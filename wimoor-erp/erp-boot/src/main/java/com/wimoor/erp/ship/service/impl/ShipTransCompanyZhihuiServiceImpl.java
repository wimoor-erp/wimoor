package com.wimoor.erp.ship.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wimoor.erp.ship.mapper.ShipTransCompayAPIMapper;
import com.wimoor.erp.ship.mapper.ShipTransCompayServicesZhihuiMapper;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayAPI;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayServicesZhihui;
import com.wimoor.erp.ship.service.IShipTransCompanyZhihuiService;
import com.wimoor.erp.util.ZmRequest;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("shipTransCompanyZhihuiService")
@RequiredArgsConstructor
public class ShipTransCompanyZhihuiServiceImpl extends ServiceImpl<ShipTransCompayServicesZhihuiMapper,ShipTransCompayServicesZhihui> implements IShipTransCompanyZhihuiService {
	final ShipTransCompanyMapper shipTransCompanyMapper;
	final ShipTransCompayAPIMapper shipTransCompayAPIMapper;
    final ShipTransCompayServicesZhihuiMapper shipTransCompayServicesZhihuiMapper;

    @Override
	public JSONObject shipTransDetial(UserInfo user, String companyid, String shipmentid,String ordernum) {
		// TODO Auto-generated method stub
		ShipTransCompayAPI companyapi = null;
        ShipTransCompany stcompany = null;
		if(!StrUtil.isEmpty(companyid)) {
			stcompany = shipTransCompanyMapper.selectById(companyid);
			if(stcompany!=null&&stcompany.getApi()!=null&&stcompany.getAccessToken()!=null) {
				  companyapi = shipTransCompayAPIMapper.selectById(stcompany.getApi());
			}
		}
		if(stcompany==null||companyapi==null) {
			return null;
		}
		if("ZM".equals(companyapi.getSystem())) {
			return 	getZmApiJson(user,companyapi,stcompany,ordernum);
		}else {
			return 	getZhApiJson(user,companyapi,stcompany,ordernum);
		}
	}
	
	public JSONObject getZhApiJson(UserInfo user ,ShipTransCompayAPI companyapi,ShipTransCompany stcompany,String  shipmentid)  {
		String url=companyapi.getApi()+"/info";
		JSONObject token = new JSONObject();
		token.put("access_token", stcompany.getAccessToken());
		JSONObject shipment = new JSONObject();
		shipment.put("shipment_id","");
		shipment.put("client_reference", shipmentid);
		JSONObject json = new JSONObject();
		json.put("validation", token);
		json.put("shipment", shipment);
		String contentType = "application/json";
		String response = null;
		try {
			response = HttpClientUtil.postUrl(url,json.toString(), null,contentType);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject result = GeneralUtil.getJsonObject(response);
		result.put("ftype", "zhapi");
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
	
	@SuppressWarnings("unchecked")
	public JSONObject getZmApiJson(UserInfo user ,ShipTransCompayAPI companyapi,ShipTransCompany stcompany,String ordernum) {
		String url = "http://ois.intelink.net:8003/ois-web";
        String appKey = "TKWDZ8907_YHECN";
        String appSecret = "9538227e1d644b33a42cd01bd4389b81";
        String nonce = "slnkda";
        String version = "1.0";
    	String body = "{\"companyNo\":\"YHECN\",\"queryType\":\"5\",\"custNo\":\"Test01\",\"noList\":\""+ordernum+"\"}";
    	ZmRequest client=new ZmRequest(appKey,appSecret,nonce,version);
    	//String token=client.getToken(url + "/auth/access/token");
    	String token = client.getToken(url + "/ois/order/getAuth");
        Map<String, String> map = JSONObject.parseObject(token, Map.class);
        token = map.get("token");
    	client.setToken(token);
    	//4 调用打单接口
        Map<String,Object> params =new HashMap<String,Object>();
        params.put("body1", body);
        client.setDatas(params);
        String result = client.request(url+"/tms/expose/queryTraceoutList", "post");
        //System.out.println("跟踪信息返回:"+result);
        JSONObject results = GeneralUtil.getJsonObject(result);
        results.put("ftype", "zmapi");
        return results;
	}
	
 
	public String getShipTransService(UserInfo user,ShipTransCompayAPI  api, ShipTransCompany accessToken, String service) {
		// TODO Auto-generated method stub
		QueryWrapper<ShipTransCompayServicesZhihui> queryone=new QueryWrapper<ShipTransCompayServicesZhihui>();
		queryone.eq("code", service);
		queryone.eq("apiid", api.getId());
		ShipTransCompayServicesZhihui result = shipTransCompayServicesZhihuiMapper.selectOne(queryone);
		if(result!=null)return result.getName();
		String url=api.getApi()+"/get_services"; 
		try {
			JSONObject token = new JSONObject();
			token.put("access_token", accessToken.getAccessToken());
			JSONObject servicesjson = new JSONObject();
			servicesjson.put("type", "all");
			JSONObject json = new JSONObject();
			json.put("validation", token);
			json.put("services", servicesjson);
			String contentType = "application/json";
			String response = HttpClientUtil.postUrl(url,json.toString(), null,contentType);
			if(!StrUtil.isEmpty(response)) {
				json=	GeneralUtil.getJsonObject(response);
				JSONObject data = json.getJSONObject("data");
				JSONArray services = data.getJSONArray("services");
				for(int i=0;i<services.size();i++) {
					JSONObject item = services.getJSONObject(i);
					String code = item.getString("code");
					String name = item.getString("name");
					String type = item.getString("type");
			 
					ShipTransCompayServicesZhihui record=new ShipTransCompayServicesZhihui();
					record.setCode(code);
					record.setFtype(type);
					record.setName(name);
					record.setApiid(api.getId());
					if(code.equals(service)) { result=record; }
					QueryWrapper<ShipTransCompayServicesZhihui> queryold=new QueryWrapper<ShipTransCompayServicesZhihui>();
					queryold.eq("code", service);
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
	public List<ShipTransCompayServicesZhihui> getShipTransService(UserInfo user,Integer  apiid, String accessToken) {
		// TODO Auto-generated method stub
		ShipTransCompayAPI companyapi = shipTransCompayAPIMapper.selectById(apiid);
		QueryWrapper<ShipTransCompayServicesZhihui> query=new QueryWrapper<ShipTransCompayServicesZhihui>();
		query.eq("apiid", apiid);
		List<ShipTransCompayServicesZhihui> result = shipTransCompayServicesZhihuiMapper.selectList(query);
		if(result!=null&&result.size()>0)return result;
		String url=companyapi.getApi()+"/get_services"; 
			JSONObject token = new JSONObject();
			token.put("access_token",accessToken);
			JSONObject servicesjson = new JSONObject();
			servicesjson.put("type", "all");
			JSONObject json = new JSONObject();
			json.put("validation", token);
			json.put("services", servicesjson);
			String contentType = "application/json";
			String response = null;
			try {
				response = HttpClientUtil.postUrl(url,json.toString(), null,contentType);
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!StrUtil.isEmpty(response)) {
				json=	GeneralUtil.getJsonObject(response);
				JSONObject data = json.getJSONObject("data");
				JSONArray services = data.getJSONArray("services");
			     result =new ArrayList<ShipTransCompayServicesZhihui>();
				for(int i=0;i<services.size();i++) {
					JSONObject item = services.getJSONObject(i);
					String code = item.getString("code");
					String name = item.getString("name");
					String type = item.getString("type");
			 
					ShipTransCompayServicesZhihui record=new ShipTransCompayServicesZhihui();
					record.setCode(code);
					record.setFtype(type);
					record.setName(name);
					record.setApiid(companyapi.getId());
					result.add(record);
					QueryWrapper<ShipTransCompayServicesZhihui> queryone=new QueryWrapper<ShipTransCompayServicesZhihui>();
					queryone.eq("code", code);
					queryone.eq("apiid", companyapi.getId());
					ShipTransCompayServicesZhihui old = shipTransCompayServicesZhihuiMapper.selectOne(queryone);
					if(old!=null) {
						shipTransCompayServicesZhihuiMapper.update(record,queryone);
					}else {
						shipTransCompayServicesZhihuiMapper.insert(record);
					}
				}
				return result;
			}else {
				return null;
			}
	}
	@Override
	public JSONObject getCreateJson(UserInfo user, String shipmentid) {
		// TODO Auto-generated method stub
		return null;
	}
	public JSONObject shipTransDetialShipment(UserInfo user, String companyid, String shipmentid) {
		// TODO Auto-generated method stub
        ShipTransCompayAPI companyapi = null;
        ShipTransCompany stcompany = null;
		if(!StrUtil.isEmpty(companyid)) {
			stcompany = shipTransCompanyMapper.selectById(companyid);
			if(stcompany!=null&&stcompany.getApi()!=null&&stcompany.getAccessToken()!=null) {
				  companyapi = shipTransCompayAPIMapper.selectById(stcompany.getApi());
			}
		}
		if(stcompany==null||companyapi==null) {
			return null;
		}
		String url=companyapi.getApi()+"/tracking";
		try {
			JSONObject token = new JSONObject();
			token.put("access_token", stcompany.getAccessToken());
			JSONObject shipment = new JSONObject();
			shipment.put("client_reference", shipmentid);
			shipment.put("parcel_number", "");
			shipment.put("language", "zh");
			JSONObject json = new JSONObject();
			json.put("validation", token);
			json.put("shipment", shipment);
			String contentType = "application/json";
			String response = HttpClientUtil.postUrl(url,json.toString(), null,contentType);
 			JSONObject result = GeneralUtil.getJsonObject(response);
			return result;
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	return null;
	}
	@Override
	public Object voidShipTransForm(UserInfo user, String shipmentid) {
		// TODO Auto-generated method stub
		return null;
	}
 
}



