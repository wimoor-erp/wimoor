package com.wimoor.amazon.auth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.apache.http.HttpException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.api.AuthorizationApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.authorization.AuthorizationCode;
import com.amazon.spapi.model.authorization.GetAuthorizationCodeResponse;
import com.amazonaws.regions.Regions;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.mapper.AmazonAuthorityMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;

import lombok.RequiredArgsConstructor;
 

/**
 * 用户业务类
 */
@Service
@RequiredArgsConstructor
public class AmazonAuthorityServiceImpl extends ServiceImpl<AmazonAuthorityMapper, AmazonAuthority> implements IAmazonAuthorityService {
    
    final IMarketplaceService iMarketplaceService;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
 
	@Autowired
	ApiBuildService apiBuildService;
	 
	
	public AmazonAuthority selectByGroupAndMarket(String groupid,String marketplaceid) {
		return this.baseMapper.selectByGroupAndMarket(groupid, marketplaceid);
	}
	
	public List<AmazonAuthority> selectByGroupId(String groupid) {
		QueryWrapper<AmazonAuthority> queryWrapper=new QueryWrapper<AmazonAuthority>();
		queryWrapper.eq("groupid", groupid);
		queryWrapper.eq("disable", Boolean.FALSE);
		return this.baseMapper.selectList(queryWrapper);
	}
	
	private String getRefreshTokenByCode(String code) throws HttpException {
		Map<String,String> body=new HashMap<String,String>();
			body.put("grant_type", "authorization_code");
			body.put("code", code);
			body.put("redirect_uri","https://app.wimoor.com");
			body.put("client_id",apiBuildService.getClientId());
			body.put("client_secret",apiBuildService.getClientSecret());
	        String response = HttpClientUtil.postUrl("https://api.amazon.com/auth/o2/token", body,null,URLEncodedUtils.CONTENT_TYPE);
	        JSONObject json = GeneralUtil.getJsonObject(response);    
	        String refresh_token = json.getString("refresh_token");
	        return refresh_token;
	}
	
	public void getRefreshTokenByRegion(String region) {
        List<AmazonAuthority> authlist = this.baseMapper.selectByRegion(region);
        for(AmazonAuthority auth:authlist) {
        	if(auth.getRefreshToken()!=null) {
        		 continue;
        	}else {
        		auth.setRefreshToken(region);
        	}
          	List<Marketplace> marketlist = iMarketplaceService.findbyauth(auth.getId());
          	
          	AuthorizationApi api = apiBuildService.getAuthorizationApi(auth);
          	String sellingPartnerId=auth.getSellerid();
          	String developerId=marketlist.get(0).getDevAccountNum();
          	String mwsAuthToken=auth.getMwsauthtoken();
       		try {
       			developerId=developerId.replace("-", "");
  				GetAuthorizationCodeResponse codeResult = api.getAuthorizationCode(sellingPartnerId,developerId, mwsAuthToken);
  				AuthorizationCode authCode = codeResult.getPayload();
  				String code=authCode.getAuthorizationCode();
  			    auth.setRefreshToken(getRefreshTokenByCode(code));
  		        this.updateById(auth);
  			} catch (ApiException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
       
	}
	
	public List<String> getAllRegion() {
		List<String> region=new ArrayList<String>();
		region.add(Regions.EU_WEST_1.getName());
		region.add(Regions.US_EAST_1.getName());
		region.add(Regions.US_WEST_2.getName());
		return region;
	}
	
	public 	List<AmazonAuthority> getAllAuth(String region) {
		List<AmazonAuthority> list = list(new LambdaQueryWrapper<AmazonAuthority>()
                .eq(AmazonAuthority::getAWSRegion, region)
                .eq(AmazonAuthority::getDisable, false)
                .isNotNull(AmazonAuthority::getRefreshToken)
                 );
		return list;
	}
	
	public 	List<AmazonAuthority> getAllAuth() {
		List<AmazonAuthority> list = list(new LambdaQueryWrapper<AmazonAuthority>()
                .eq(AmazonAuthority::getDisable, false)
                .isNotNull(AmazonAuthority::getRefreshToken)
                 );
		return list;
	}
	
	public void executTask(IRunAmazonService apiService) {
	    List<String> region = getAllRegion() ;
		List<Runnable> runlist=new ArrayList<Runnable>();
		for(String item:region) {
			runlist.add(getRunnable(apiService,item));
		}
		executThread(runlist,60);
	}
	

	public  Runnable getRunnable(IRunAmazonService apiService,String region) {
		return new Runnable() {
			public void run() { 
				      List<AmazonAuthority> list = getAllAuth(region);
				      for(AmazonAuthority auth:list) { 
				    	  apiService.runApi(auth);
				      }
			}
		};
	}

 

	public   void executThread(List<Runnable> runnables, int waitMinute) {
		if (runnables == null) {
			return;
		}
		List<Future<?>> threadList = new ArrayList<Future<?>>();
		for (int i = 0; i < runnables.size(); i++) {
			if (runnables.get(i) != null) {
				threadList.add(threadPoolTaskExecutor.submit(runnables.get(i)));
			}
		}
		int time = 1;
		boolean alldone = false;
		while (alldone == false && time < waitMinute*60) {
			alldone = true;
			for (int i = 0; i < threadList.size(); i++) {
				if (!threadList.get(i).isDone()) {
					alldone = false;
					break;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			time++;
		}
		for (int i = 0; i < threadList.size(); i++) {
			if (!threadList.get(i).isDone()) {
				threadList.get(i).cancel(true);
			}
		}
		BlockingQueue<Runnable> que = threadPoolTaskExecutor.getThreadPoolExecutor().getQueue();
		if (que != null && que.size() > 0) {
			for (int i = 0; i < runnables.size(); i++) {
				Runnable runnable = runnables.get(i);
				if (i<threadList.size()&&!threadList.get(i).isDone()) {
					threadList.get(i).cancel(true);
					que.remove(runnable);
				}
			}
		}
	}

	@Override
	public List<AmazonAuthority> selectByMarket(String marketplaceid) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectByMarket(marketplaceid);
	}

	@Override
	public AmazonAuthority selectBySellerId(String sellerid) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectBySellerId(sellerid);
	}
	
	@Override
	public AmazonAuthority authSeller(String state, String selling_partner_id, String mws_auth_token,
			String spapi_oauth_code) {
		// TODO Auto-generated method stub
		AmazonAuthority auth=new AmazonAuthority();
		auth.setSellerid(selling_partner_id);
		auth.setMwsauthtoken(mws_auth_token);
		String[] groupRegion = state.split("@");
		auth.setGroupid(groupRegion[0]);
		auth.setAWSRegion(groupRegion[1]);
		try {
			String refreshcode = this.getRefreshTokenByCode(spapi_oauth_code);
			auth.setRefreshToken(refreshcode);
			this.save(auth);
			
			return auth;
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAuthUrl(String groupid, String marketplaceid) {
		// TODO Auto-generated method stub
    	Marketplace market = iMarketplaceService.getById(marketplaceid);
    	String result="";
    	if(market.getAwsRegion().equals("eu-west-1")){
    		result="https://sellercentral-europe.amazon.com";
    	}else {
    		result="https://sellercentral."+market.getPointName().toLowerCase();
    	}
    	result+="/apps/authorize/consent?application_id="+apiBuildService.getAppid()+"&state="+groupid+"@"+market.getAwsRegion()+"&version=beta";
    	result+="&redirect_uri=https://app.wimoor.com/amazon/auth";
		return result;
	}
   
     
}