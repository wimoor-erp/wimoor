package com.wimoor.erp.thirdparty.service.impl;

import cn.hutool.core.util.StrUtil;
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
import com.wimoor.erp.thirdparty.mapper.ThirdPartyAPIMapper;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.service.IShipTransCompanyZMService;
import com.wimoor.erp.thirdparty.service.IShipTransCompanyZhihuiService;
import com.wimoor.erp.util.ZmRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("shipTransCompanyZMService")
@RequiredArgsConstructor
public class ShipTransCompanyZMServiceImpl   implements IShipTransCompanyZMService {
	final ShipTransCompanyMapper shipTransCompanyMapper;
	final ThirdPartyAPIMapper thirdPartyAPIMapper;

	@SuppressWarnings("unchecked")
	public JSONObject getApiJson(UserInfo user , ThirdPartyAPI companyapi, ShipTransCompany stcompany, String ordernum) {
        String nonce = "slnkda";
        String version = "1.0";
    	String body = "{\"companyNo\":\"YHECN\",\"queryType\":\"5\",\"custNo\":\"Test01\",\"noList\":\""+ordernum+"\"}";
    	ZmRequest client=new ZmRequest(companyapi.getAppkey(),companyapi.getAppsecret(),nonce,version);
    	//String token=client.getToken(url + "/auth/access/token");
    	String token = client.getToken(companyapi.getApi() + "/ois/order/getAuth");
        Map<String, String> map = JSONObject.parseObject(token, Map.class);
        token = map.get("token");
    	client.setToken(token);
    	//4 调用打单接口
        Map<String,Object> params =new HashMap<String,Object>();
        params.put("body1", body);
        client.setDatas(params);
        String result = client.request(companyapi.getApi()+"/tms/expose/queryTraceoutList", "post");
        //System.out.println("跟踪信息返回:"+result);
        JSONObject results = GeneralUtil.getJsonObject(result);
        results.put("ftype", "ZM");
        return results;
	}


 
}



