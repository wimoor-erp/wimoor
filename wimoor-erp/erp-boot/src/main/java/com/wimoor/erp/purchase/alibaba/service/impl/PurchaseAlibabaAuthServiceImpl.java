package com.wimoor.erp.purchase.alibaba.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseAlibabaAuthMapper;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaAuth;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaAuthService;
import com.wimoor.erp.util.CommonUtil;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
 

@Service("purchaseAlibabaAuthService")
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "alibaba")
public class PurchaseAlibabaAuthServiceImpl extends ServiceImpl<PurchaseAlibabaAuthMapper,PurchaseAlibabaAuth> implements IPurchaseAlibabaAuthService {
    
	@Setter
    String appKey;
    @Setter
    String appSecret;
    @Setter
    String open1688;
    
	public static SimpleDateFormat alibabatimefmt = new SimpleDateFormat("yyyyMMddhhmmssSSS");

	public PurchaseAlibabaAuth saveAction(PurchaseAlibabaAuth purchaseAlibabaAuth) {
		QueryWrapper<PurchaseAlibabaAuth> queryWrapper=new QueryWrapper<PurchaseAlibabaAuth>();
		queryWrapper.eq("name", purchaseAlibabaAuth.getName());
		queryWrapper.eq("shopid", purchaseAlibabaAuth.getShopid());
		queryWrapper.eq("isDelete", 0);
		PurchaseAlibabaAuth purchaseAlibabaAuth_old = this.getOne(queryWrapper);
		if (purchaseAlibabaAuth.idIsNULL()) {
			if (purchaseAlibabaAuth_old != null) {
				throw new ERPBizException("店铺名称重复!");
			}
			if(StrUtil.isBlank(purchaseAlibabaAuth.getAppkey())||purchaseAlibabaAuth.getAppkey().equals("******")) {
				purchaseAlibabaAuth.setAppkey(appKey);
			}
			if(StrUtil.isBlank(purchaseAlibabaAuth.getAppsecret())||purchaseAlibabaAuth.getAppsecret().equals("**********")) {
				purchaseAlibabaAuth.setAppsecret(appSecret);
			}
			purchaseAlibabaAuth.setAppkey(purchaseAlibabaAuth.getAppkey().trim());
			purchaseAlibabaAuth.setAppsecret(purchaseAlibabaAuth.getAppsecret().trim());
			this.save(purchaseAlibabaAuth);
		} else {
			if (purchaseAlibabaAuth_old != null && !purchaseAlibabaAuth_old.getId().equals(purchaseAlibabaAuth.getId())) {
				throw new ERPBizException("店铺名称重复!");
			}else {
				purchaseAlibabaAuth_old=this.getById(purchaseAlibabaAuth.getId());
			}
			if(StrUtil.isBlank(purchaseAlibabaAuth.getAppkey())) {
				purchaseAlibabaAuth.setAppkey(appKey);
			}
			if(purchaseAlibabaAuth.getAppkey().equals("******")) {
				purchaseAlibabaAuth.setAppkey(purchaseAlibabaAuth_old.getAppkey());
			}
			if(StrUtil.isBlank(purchaseAlibabaAuth.getAppsecret())) {
				purchaseAlibabaAuth.setAppsecret(appSecret);
			}
			if(purchaseAlibabaAuth.getAppsecret().equals("**********")) {
				purchaseAlibabaAuth.setAppsecret(purchaseAlibabaAuth_old.getAppsecret());
			}
			purchaseAlibabaAuth.setAppkey(purchaseAlibabaAuth.getAppkey().trim());
			purchaseAlibabaAuth.setAppsecret(purchaseAlibabaAuth.getAppsecret().trim());
			purchaseAlibabaAuth_old.setOperator(purchaseAlibabaAuth.getOperator());
			purchaseAlibabaAuth_old.setOpttime(purchaseAlibabaAuth.getOpttime());
			purchaseAlibabaAuth_old.setName(purchaseAlibabaAuth.getName());
			purchaseAlibabaAuth_old.setAppkey(purchaseAlibabaAuth.getAppkey());
			purchaseAlibabaAuth_old.setAppsecret(purchaseAlibabaAuth.getAppsecret());
			this.updateById(purchaseAlibabaAuth_old);
		}
		return purchaseAlibabaAuth;
	}
    
	public PurchaseAlibabaAuth refreshAuthToken(PurchaseAlibabaAuth purchaseAlibabaAuth) {
		if (new Date().after(purchaseAlibabaAuth.getRefreshTokenTimeout())) {
		   throw new ERPBizException("长效令牌失效，请到授权模块，1688绑定页面重新进行绑定。长效令牌时效半年");
		   //if(refreshAuthRefreshToken(purchaseAlibabaAuth)==null)return null;
		}
		String url = "https://gw.open.1688.com/openapi/param2/1/system.oauth2/getToken/" + purchaseAlibabaAuth.getAppkey();
		String contentType = "application/x-www-form-urlencoded";
		Map<String, String> props = new HashMap<String, String>();
		props.put("grant_type", "refresh_token");
		props.put("client_id", purchaseAlibabaAuth.getAppkey());
		props.put("client_secret", purchaseAlibabaAuth.getAppsecret());
		props.put("refresh_token", purchaseAlibabaAuth.getRefreshToken());
		props.put("Content-Type", contentType);
		JSONObject json = postUrl(url,props);
		String access_token = json.getString("access_token");
		String aliId = json.getString("aliId");
		String resource_owner = json.getString("resource_owner");
		int expires_in = json.getInteger("expires_in");
		String memberId = json.getString("memberId");
		purchaseAlibabaAuth.setAccessToken(access_token);
		purchaseAlibabaAuth.setAliId(new BigInteger(aliId));
		purchaseAlibabaAuth.setResourceOwner(resource_owner);
		Date afterDate = new Date(new Date().getTime() + (expires_in - 300) * 1000);
		purchaseAlibabaAuth.setAccessTokenTimeout(afterDate);
		purchaseAlibabaAuth.setMemberId(memberId);
		purchaseAlibabaAuth.setOpttime(new Date());
	    this.updateById(purchaseAlibabaAuth);
		return purchaseAlibabaAuth;
	}

	public PurchaseAlibabaAuth refreshAuthRefreshToken(PurchaseAlibabaAuth purchaseAlibabaAuth) {
		String url = "https://gw.open.1688.com/openapi/param2/1/system.oauth2/postponeToken/" + purchaseAlibabaAuth.getAppkey();
		url+="?access_token="+ purchaseAlibabaAuth.getAccessToken();
		url+="&client_id="+purchaseAlibabaAuth.getAppkey();
		url+="&client_secret="+ purchaseAlibabaAuth.getAppsecret();
		url+="&refresh_token="+purchaseAlibabaAuth.getRefreshToken();
		JSONObject json = getUrl(url);
		if(json.containsKey("iserror")==true) {
			throw new ERPBizException("刷新token失败！");
		}
		String access_token = json.getString("access_token");
		String aliId = json.getString("aliId");
		String refresh_token = json.getString("refresh_token");
		String resource_owner = json.getString("resource_owner");
		int expires_in = json.getInteger("expires_in");
		String refresh_token_timeout = json.getString("refresh_token_timeout");
		String memberId = json.getString("memberId");
		purchaseAlibabaAuth.setAccessToken(access_token);
		purchaseAlibabaAuth.setRefreshToken(refresh_token);
		purchaseAlibabaAuth.setAliId(new BigInteger(aliId));
		purchaseAlibabaAuth.setResourceOwner(resource_owner);
		Date afterDate = new Date(new Date().getTime() + (expires_in - 300) * 1000);
		purchaseAlibabaAuth.setAccessTokenTimeout(afterDate);
		purchaseAlibabaAuth.setMemberId(memberId);
		purchaseAlibabaAuth.setOpttime(new Date());
		try {
			Date refreshtime = alibabatimefmt.parse(refresh_token_timeout);
			purchaseAlibabaAuth.setRefreshTokenTimeout(refreshtime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.baseMapper.updateById(purchaseAlibabaAuth);
		return purchaseAlibabaAuth;
	}
	public String getJuShiTa() {
		//聚石塔配置在wimoor-auth模块，将该模块放置在可以访问1688的服务器上面。
		if(open1688!=null)return open1688;
		else {
			return "https://alibaba.wimoor.com";
		}
	}
	public JSONObject callApi(String url,String appsecret ,Map<String, String> param) {
		String jushita=getJuShiTa()+"/api/v1/open1688/callApi";
		param.put("url", url);
		param.put("appsecret", appsecret);
    	String s =null;
    	try {
			  s = HttpClientUtil.postUrl(jushita, param, HttpClientUtil.getHeader(),"application/json" );
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(StrUtil.isBlankOrUndefined(s)) {
        	throw new ERPBizException("操作失败！请联系管理员");
        }
		JSONObject json = GeneralUtil.getJsonObject(s);
		if(json==null) {
			throw new ERPBizException("操作失败！请联系管理员");
		}
		if(json.containsKey("iserror")==true) {
				throw new ERPBizException("操作失败！"+json.getString("msg"));
		}
		return json;
	}
    public JSONObject postUrl(String url,Map<String, String> props) {
    	String jushita=getJuShiTa()+"/api/v1/open1688/postUrl";
    	props.put("url", url);
    	String s =null;
    	try {
			  s = HttpClientUtil.postUrl(jushita, props, HttpClientUtil.getHeader(),"application/json" );
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(StrUtil.isBlankOrUndefined(s)) {
        	throw new ERPBizException("操作失败！请联系管理员");
        }
		JSONObject json = GeneralUtil.getJsonObject(s);
		if(json==null) {
			throw new ERPBizException("操作失败！请联系管理员");
		}
		if(json.containsKey("iserror")==true) {
				throw new ERPBizException("绑定失败！"+json.getString("msg"));
		}
		return json;
    }
   
    @SuppressWarnings("deprecation")
	public JSONObject getUrl(String url) {
    	url=getJuShiTa()+"/api/v1/open1688/getUrl?url="+java.net.URLEncoder.encode(url);
		String s = null;
		try {
			s = HttpClientUtil.getUrl(url,null);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StrUtil.isBlankOrUndefined(s)) {
        	throw new ERPBizException("操作失败！请联系管理员");
        }
		JSONObject json = GeneralUtil.getJsonObject(s);
		if(json==null) {
			throw new ERPBizException("操作失败！请联系管理员");
		}
		if(json.containsKey("iserror")==true) {
				throw new ERPBizException("绑定失败！"+json.getString("msg"));
		}
		return json;
    }
	public PurchaseAlibabaAuth bindAuth(UserInfo user, String code, String state) {
		PurchaseAlibabaAuth nowPurchaseAlibabaAuth = super.getById(state);
	    String mappKey=appKey;
	    String mappSecret=appSecret;
	    if(nowPurchaseAlibabaAuth!=null) {
	    	mappKey=nowPurchaseAlibabaAuth.getAppkey();
	    	mappSecret=nowPurchaseAlibabaAuth.getAppsecret();
	    }else {
	    	nowPurchaseAlibabaAuth=new PurchaseAlibabaAuth();
	    	nowPurchaseAlibabaAuth.setAppkey(mappKey);
	    	nowPurchaseAlibabaAuth.setAppsecret(mappSecret);
	    }
		String url = "https://gw.open.1688.com/openapi/http/1/system.oauth2/getToken/" + mappKey;
		url+="?grant_type=authorization_code";
		url+="&need_refresh_token=true";
		url+="&code="+ code.trim();
		url+="&client_id="+mappKey.trim();
		url+="&client_secret="+ mappSecret.trim();
		url+="&redirect_uri=http://localhost:12315";
		PurchaseAlibabaAuth purchaseAlibabaAuth = null;
			JSONObject json = getUrl(url);
			String access_token = json.getString("access_token");
			String aliId = json.getString("aliId");
			String refresh_token = json.getString("refresh_token");
			String resource_owner = json.getString("resource_owner");
			int expires_in = json.getInteger("expires_in");
			String refresh_token_timeout = json.getString("refresh_token_timeout");
			String memberId = json.getString("memberId");
			Date afterDate = new Date(new Date().getTime() + (expires_in - 300) * 1000);
			QueryWrapper<PurchaseAlibabaAuth> queryWrapper=new QueryWrapper<PurchaseAlibabaAuth>();
		 
			queryWrapper.eq("aliId", aliId);
			queryWrapper.eq("shopid",user.getCompanyid());
			queryWrapper.eq("memberId", memberId);
			queryWrapper.eq("resource_owner", resource_owner);
			queryWrapper.eq("refresh_token", refresh_token);
			try {
				purchaseAlibabaAuth = this.getOne(queryWrapper);
			}catch(Exception e) {
				e.printStackTrace();
			}
			if (purchaseAlibabaAuth != null) {
				purchaseAlibabaAuth.setName(nowPurchaseAlibabaAuth.getName());
				purchaseAlibabaAuth.setDelete(false);
				if (GeneralUtil.isNotEmpty(nowPurchaseAlibabaAuth.getAppkey())) {
					purchaseAlibabaAuth.setAppkey(nowPurchaseAlibabaAuth.getAppkey());
					purchaseAlibabaAuth.setAppsecret(nowPurchaseAlibabaAuth.getAppsecret());
				}
			} else {
				purchaseAlibabaAuth = nowPurchaseAlibabaAuth;
			}
			purchaseAlibabaAuth.setAccessToken(access_token);
			purchaseAlibabaAuth.setRefreshToken(refresh_token);
			purchaseAlibabaAuth.setAliId(new BigInteger(aliId));
			purchaseAlibabaAuth.setResourceOwner(resource_owner);
			purchaseAlibabaAuth.setMemberId(memberId);
			purchaseAlibabaAuth.setAccessTokenTimeout(afterDate);
			purchaseAlibabaAuth.setOpttime(new Date());
			if(purchaseAlibabaAuth.idIsNULL()) {
				purchaseAlibabaAuth.setName(resource_owner);
			}
			try {
				Date refreshtime = alibabatimefmt.parse(refresh_token_timeout);
				purchaseAlibabaAuth.setRefreshTokenTimeout(refreshtime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.baseMapper.updateById(purchaseAlibabaAuth);
	 
		return purchaseAlibabaAuth;
	}

	public List<PurchaseAlibabaAuth> getAuthData(UserInfo user) {
		QueryWrapper<PurchaseAlibabaAuth> queryWrapper=new QueryWrapper<PurchaseAlibabaAuth>();
		queryWrapper.eq("shopid", user.getCompanyid());
		queryWrapper.eq("isDelete", 0);
		List<PurchaseAlibabaAuth> list = this.list(queryWrapper);
		for(PurchaseAlibabaAuth item:list) {
			item.setAppkey("******");
			item.setAppsecret("**********");
		}
		return list;
	}

	public static ApiExecutor getApiExecutor(PurchaseAlibabaAuth alibabaAuth) {
		return new ApiExecutor(alibabaAuth.getAppkey(), alibabaAuth.getAppsecret());
	}
    public boolean checkAuthorityToken(PurchaseAlibabaAuth alibabaAuth) {
    	if (alibabaAuth != null && alibabaAuth.getAccessTokenTimeout() != null && new Date().after(alibabaAuth.getAccessTokenTimeout())) {
			if(this.refreshAuthToken(alibabaAuth)==null)return true;
		}
    	return false;
    }
	// 获取物流的跟踪信息
	public JSONObject captureLogisticsTraceInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid) {
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("webSite", "1688");
		param.put("orderId", alibabaOrderid);
		if(alibabaAuth==null) {
			return null;
		}
		param.put("access_token", alibabaAuth.getAccessToken());
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.logistics",
				"alibaba.trade.getLogisticsTraceInfo.buyerView", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json =  callApi(urlPath, alibabaAuth.getAppsecret(), param);
		if (json.containsKey("errorMessage")) {
			Object errorMsg = json.get("errorMessage");
			if (errorMsg != null) {
				return null;
			}
		}
		return json;
	}

	public JSONObject captureLogisticsInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid) {
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("webSite", "1688");
		param.put("orderId", alibabaOrderid);
		param.put("access_token", alibabaAuth.getAccessToken());
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.logistics",
				"alibaba.trade.getLogisticsInfos.buyerView", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json= callApi(urlPath, alibabaAuth.getAppsecret(), param);
		if (json.get("errorMessage") != null) {
			Object errorMsg = json.get("errorMessage");
			if (errorMsg != null) {
				return null;
			}
		}
		return json;
	}

	public JSONObject captureOrderFromAlibaba(UserInfo user, String alibabaAuthid, String alibabaOrderid) {
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		if (alibabaAuth == null) {
			return null;
		}
		if(alibabaAuth.isDelete()==false) {
			checkAuthorityToken(alibabaAuth);
			Map<String, String> param = new HashMap<String, String>();
			param.put("webSite", "1688");
			param.put("orderId", alibabaOrderid);
			param.put("access_token", alibabaAuth.getAccessToken());
			String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.trade.get.buyerView", 1, "param2", alibabaAuth.getAppkey());
			JSONObject json = this.callApi(urlPath,alibabaAuth.getAppsecret(),param);
			return json;
		}else {
			return null; 
		}

	}

	public String captureRefundByOrderId(UserInfo user, Map<String, String> map) {
		if (map == null || map.size() < 0) {
			return null;
		}
		String alibabaAuthid = map.get("alibabaAuthid");
		String alibabaOrderid = map.get("alibabaOrderid");
		String queryType = map.get("queryType");
		String dipsuteType = map.get("dipsuteType");
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("orderId", alibabaOrderid);
		param.put("queryType", queryType);
		param.put("dipsuteType", dipsuteType);
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade",
				"alibaba.trade.refund.OpQueryBatchRefundByOrderIdAndStatus", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json =callApi(urlPath, alibabaAuth.getAppsecret(), param);
		if ("order.find.error".equals(json.get("errorCode"))) {
			Object errorMsg = json.get("errorMessage");
			if (errorMsg != null) {
				throw new ERPBizException(errorMsg.toString());
			}
		}
		return json.toString();
	}

	public PurchaseAlibabaAuth updateAlibaba(UserInfo user, String id) {
		PurchaseAlibabaAuth purchaseAlibabaAuth = super.getById(id);
		if (purchaseAlibabaAuth != null) {
			purchaseAlibabaAuth.setDelete(true);
			purchaseAlibabaAuth.setOpttime(new Date());
			purchaseAlibabaAuth.setOperator(user.getId());
			this.updateById(purchaseAlibabaAuth);
		} else {
			throw new ERPBizException("请选择正确的账号解绑！");
		}
		return purchaseAlibabaAuth;
	}

	@Override
	public PurchaseAlibabaAuth selectBymemberId(String memberid) {
		QueryWrapper<PurchaseAlibabaAuth> queryWrapper=new QueryWrapper<PurchaseAlibabaAuth>();
		queryWrapper.eq("memberId", memberid);
		List<PurchaseAlibabaAuth> list = this.baseMapper.selectList(queryWrapper);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	public String getAppKey(String id) {
		PurchaseAlibabaAuth auth = this.baseMapper.selectById(id);
		if(StrUtil.isBlank(auth.getAppkey())) {
			return appKey;
		}else {
			return auth.getAppkey();
		}
	}

	@Override
	@Cacheable(value = "PurchaseAlibabaCache#60",key="#id")
	public Object getAddress(String id) {
		// TODO Auto-generated method stub
		PurchaseAlibabaAuth alibabaAuth = this.getById(id);
		checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", alibabaAuth.getAccessToken());
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.trade.receiveAddress.get", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = this.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}

	@Override
	public JSONObject addFeedback(String alibabaAuthid, String alibabaOrderid, String remark) {
		// TODO Auto-generated method stub
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", alibabaAuth.getAccessToken());
		JSONObject object=new JSONObject();
		object.put("feedback", remark);
		object.put("orderId", alibabaOrderid);
		param.put("tradeFeedbackParam",object.toJSONString());
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.trade.addFeedback", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = this.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}

	@Override
	public JSONObject cancelOrder(String alibabaAuthid, String alibabaOrderid, String cancelReason, String remark) {
		// TODO Auto-generated method stub
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("webSite", "1688");
		param.put("access_token", alibabaAuth.getAccessToken());
		param.put("tradeID",alibabaOrderid);
		param.put("cancelReason",cancelReason);
		param.put("remark",remark);
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.trade.cancel", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = this.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}
 
	@Override
	public JSONObject productInfo(String alibabaAuthid, String productId) {
		// TODO Auto-generated method stub
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", alibabaAuth.getAccessToken());
		param.put("productId",productId);
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.product","alibaba.cross.productInfo", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = this.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}
	
	@Override
	public JSONObject syncProductListPushed(String alibabaAuthid, String productId) {
		// TODO Auto-generated method stub
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", alibabaAuth.getAccessToken());
		JSONArray object=new JSONArray();
		object.add(productId);
		param.put("productIdList",object.toString());
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.product.push","alibaba.cross.syncProductListPushed", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = this.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}
	
}
