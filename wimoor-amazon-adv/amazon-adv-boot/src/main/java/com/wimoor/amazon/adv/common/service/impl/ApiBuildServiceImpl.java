package com.wimoor.amazon.adv.common.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzRegion;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvHttpClientResponseHandler;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.utils.HttpClientUtil;
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.util.StringUtil;

@Service("apiBuildService")
public class ApiBuildServiceImpl implements ApiBuildService {
    Date serverBusyTime=null;
    @Lazy
    @Autowired
    IAmzAdvAuthService amzAdvAuthService;
 
	public void setBusyTime(Date date) {
		// TODO Auto-generated method stub
		serverBusyTime=date;
	}
	
    public boolean isBusy() {
		if(serverBusyTime==null) {
			Calendar c=Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			serverBusyTime=c.getTime();
		}
    	return GeneralUtil.distanceOfSecond(serverBusyTime, new Date())<10;
    }
	private void handlerBaseException(BaseException e) {
		e.printStackTrace();//HTTP 401 Unauthorized
		if(e.getMessage()!=null) {
			if (e.getMessage().contains("Server is busy. Try again later") 
					||"Too Many Requests".equals(e.getMessage())
					|| e.getMessage().contains("timed out")
					||e.getMessage().contains("Remote host closed connection during handshake")
					|| e.getCode().contains("Too Many Requests")) {
				BaseException be = new BaseException("亚马逊系统繁忙！");
				serverBusyTime=new Date();
				be.setCode(BaseException.AmazonBusy);
				throw be;
			}else {
				throw e;
			}
		}
	}
	 

	public String amzAdvPut_V2(AmzAdvProfile profile, String url, String jsonparam) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzAdvAuthService.getRegion(amzadvauth.getRegion());
		url = "https://" + regionObject.getAdvpoint() + "/v2" + url;
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
		try {
			response = HttpClientUtil.putUrl(url, jsonparam, header);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return response;
	}


	public String amzAdvPut(AmzAdvProfile profile, String url, String jsonparam) {
		AmzAdvAuth amzadvauth =amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		url = "https://" + amzadvauth.getRegionObject().getAdvpoint() + url;
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", amzadvauth.getRegionObject().getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
		try {
			response = HttpClientUtil.putUrl(url, jsonparam, header);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public String amzAdvPut(AmzAdvProfile profile, String url, String jsonparam,Map<String, String> headerext) {
		AmzAdvAuth amzadvauth =amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		url = "https://" + amzadvauth.getRegionObject().getAdvpoint() + url;
		Map<String, String> header = new HashMap<String, String>();
		header.putAll(headerext);
		header.put("Amazon-Advertising-API-ClientId", amzadvauth.getRegionObject().getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
		try {
			response = HttpClientUtil.putUrl(url, jsonparam, header);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public String amzAdvGet_V2(AmzAdvProfile profile, String url) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			return null;
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
 
		if(regionObject == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		url = "https://" + regionObject.getAdvpoint() + "/v2" + url;
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response   =null;
		try {
		         response   = HttpClientUtil.getUrl(url, header);
				} catch (BaseException e) {
					if(e.getMessage()!=null) {
						amzAdvAuthService.checkProfileMessage(profile, e.getMessage());
					}
					handlerBaseException(e);
					if(e.getMessage()!=null && (e.getMessage().contains("HTTP 401 Unauthorized")
							||e.getMessage().contains("Requested recommendation is not available"))) {
						throw new BaseException(e.getMessage());
					}
			}
 
		return response;
	}

	public String amzAdvGet(AmzAdvProfile profile, String url,String contentType) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		url = "https://" + regionObject.getAdvpoint() + url;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
			try {
				response = HttpClientUtil.getUrl(url, header);
			} catch (BaseException e) {
				if(e.getMessage()!=null) {
					amzAdvAuthService.checkProfileMessage(profile, e.getMessage());
				}
				handlerBaseException(e);
				throw e;
			}
		return response;
	}
	
	public String amzAdvGet(AmzAdvProfile profile, String url) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		url = "https://" + regionObject.getAdvpoint() + url;
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
		int i = 0;
		while (i < 3) {
			try {
				response = HttpClientUtil.getUrl(url, header);
				i = 100;
			} catch (BaseException e) {
				if (e.getMessage() != null && "Too Many Requests".equals(e.getMessage())) {
					serverBusyTime=new Date();
					}
					e.printStackTrace();
					break;
				 }
		}
		return response;
	}

	public String amzAdvGet(AmzAdvProfile profile, String url,Map<String,String> headerExt) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		url = "https://" + regionObject.getAdvpoint() + url;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		header.putAll(headerExt);
		String response = null;
		int i = 0;
		while (i < 3) {
			try {
				response = HttpClientUtil.getUrl(url, header);
				i = 100;
			} catch (BaseException e) {
				if (e.getMessage() != null && "Too Many Requests".equals(e.getMessage())) {
					serverBusyTime=new Date();
					}
					e.printStackTrace();
					break;
				 }
		}
		return response;
	}
	public String amzAdvDelete_V2(AmzAdvProfile profile, String url) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		url = "https://" + regionObject.getAdvpoint() + "/v2" + url;
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
		try {
			response = HttpClientUtil.deleteUrl(url, header);
		} catch (BaseException e) {
			handlerBaseException(e);
		}
		return response;
	}

	public String amzAdvDelete(AmzAdvProfile profile, String url,Map<String,String> headerExt) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		url = "https://" + regionObject.getAdvpoint() + url;
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
		try {
			response = HttpClientUtil.deleteUrl(url, header);
		} catch (BaseException e) {
			handlerBaseException(e);
		}
		return response;
	}

	public String amzAdvDelete(AmzAdvProfile profile, String url) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		url = "https://" + regionObject.getAdvpoint() + url;
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
		try {
			response = HttpClientUtil.deleteUrl(url, header);
		} catch (BaseException e) {
			handlerBaseException(e);
		}
		return response;
	}
	
	public String amzAdvDownload(AmzAdvProfile profile, String url) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		if (url == null) {
			throw new BaseException("亚马逊还未生成报表，请稍后再试");
		}
		String response = null;
		String responseJson = null;
		try {
			response = HttpClientUtil.getRedirectInfo(url, header);
			if (StringUtil.isEmpty(response))
				return null;
			responseJson = HttpClientUtil.getUrl(response, null);
		} catch (BaseException e) {
			handlerBaseException(e);
		}
		return responseJson;
	}
	
	public String amzAdvDownloadGZIP(AmzAdvProfile profile, String url,String snid) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		header.put("snapshotId", snid);
		if (url == null) {
			throw new BaseException("亚马逊还未生成报表，请稍后再试");
		}
		String response = null;
		String responseJson = null;
		try {
            response = HttpClientUtil.getRedirectInfo(url, header);
			if (StringUtil.isEmpty(response)) {
				return null;
			}
			responseJson = HttpClientUtil.getGZFileUrl(response);
		} catch (BaseException e) {
			handlerBaseException(e);
			throw e;
		}
		return responseJson;
	}
	
	public boolean amzAdvDownloadFile(AmzAdvProfile profile, AmzAdvRequest request, IAmzAdvHttpClientResponseHandler responseHandler) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			request.setLog("无法获取所在站点权限");
			request.setTreatStatus("error");
			return true;
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		String contentType ="application/json";
		if(request.getCampaigntype().equals("sp")) {
			  contentType = "application/vnd.createasyncreportrequest.v3+json";
		}
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String url = request.getLocation();
		if(url!=null&&!url.contains("https://offline-report")) {
			try {
				url = HttpClientUtil.getRedirectInfo(url, header);
			} catch (BaseException e) {
				handlerBaseException(e);
				request.setLog("getRedirectInfo异常，"+e.getMessage());
				request.setTreatStatus("error");
				return true;
			}
		}
		if (url == null) {
			request.setLog("localtion为空，无法获取到报表。");
			request.setTreatStatus("error");
			return true;
		}
		if (StringUtil.isNotEmpty(url)) {
			return HttpClientUtil.getGZFileUrl(url, profile, request, responseHandler);
		} 
		return false;
	}
	

	public String amzAdvPost_V2(AmzAdvProfile profile, String url, String jsonparam) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if (amzadvauth == null) {
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzadvauth.getRegionObject();
		if (regionObject == null) {
			throw new BaseException("无法获取所在区域");
		}
		url = "https://" + regionObject.getAdvpoint() + "/v2" + url;
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
				try {
					response = HttpClientUtil.postUrl(url, jsonparam, header);
				} catch (BaseException e) {
					if(e.getMessage()!=null) {
						amzAdvAuthService.checkProfileMessage(profile, e.getMessage());
					}
					if(e.getMessage().contains("Unauthorized")) {
						System.out.println(url);
						System.out.println(profile.getId());
					}
					handlerBaseException(e);
					throw e;
			    }
		return response;
	}


  
	public String amzAdvPost(AmzAdvProfile profile, String url, String jsonparam,Map<String, String> headerext) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		AmzRegion regionObject =amzadvauth.getRegionObject();
		url = "https://" + regionObject.getAdvpoint() + url;
		Map<String, String> header = new HashMap<String, String>();
		header.putAll(headerext);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
		try {
			response = HttpClientUtil.postUrl(url, jsonparam, header);
		} catch (BaseException e) {
			if(e.getMessage()!=null) {
				amzAdvAuthService.checkProfileMessage(profile, e.getMessage());
			}
			handlerBaseException(e);
			throw e;
		}
	 
		return response;
	}
	
	public String amzAdvPost(AmzAdvProfile profile, String url, String jsonparam) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		AmzRegion regionObject = amzadvauth.getRegionObject();
		url = "https://" + regionObject.getAdvpoint() + url;
		String contentType = "application/json";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", contentType);
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
				try {
					response = HttpClientUtil.postUrl(url, jsonparam, header);
				} catch (BaseException e) {
					if(e.getMessage()!=null) {
						amzAdvAuthService.checkProfileMessage(profile, e.getMessage());
					}
					handlerBaseException(e);
			 
				}
		return response;
	}
 
	public String amzAdvAssetsPost(AmzAdvProfile profile, String url, Map<String, Object> jsonparam) {
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		AmzRegion regionObject = amzadvauth.getRegionObject();
		url = "https://" + regionObject.getAdvpoint() + url;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Disposition", jsonparam.get("fullname").toString());
		header.put("Amazon-Advertising-API-ClientId", regionObject.getClientId());
		header.put("Authorization", amzadvauth.getAccessToken());
		header.put("Amazon-Advertising-API-Scope", profile.getId().toString());
		String response = null;
		try {
			response = HttpClientUtil.postUrl(url, jsonparam, header);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return response;
	}

 

}
