package com.wimoor.amazon.auth.service.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.http.HttpException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.api.AuthorizationApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.authorization.AuthorizationCode;
import com.amazon.spapi.model.authorization.GetAuthorizationCodeResponse;
import com.amazonaws.regions.Regions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.mapper.AmazonAuthorityMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.AmazonSellerMarket;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.pojo.vo.AmazonGroupVO;
import com.wimoor.amazon.auth.pojo.vo.AmazonMarketVO;
import com.wimoor.amazon.auth.pojo.vo.AmazonRegionVO;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IAmazonSellerMarketService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.report.mapper.AmazonAuthMarketPerformanceMapper;
import com.wimoor.amazon.report.pojo.entity.AmazonAuthMarketPerformance;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IHandlerReportService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 

/**
 * 用户业务类
 */
@Service
@RequiredArgsConstructor
public class AmazonAuthorityServiceImpl extends ServiceImpl<AmazonAuthorityMapper, AmazonAuthority> implements IAmazonAuthorityService {
    
    final IMarketplaceService iMarketplaceService;
	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
    AmazonAuthMarketPerformanceMapper amazonAuthMarketPerformanceMapper;
	@Autowired
	ApiBuildService apiBuildService;
	@Autowired
	@Lazy
	IHandlerReportService handlerReportService;
	@Autowired
    IAmazonSellerMarketService iAmazonSellerMarketService;
	
	final IAmazonGroupService iAmazonGroupService;
	 
	
	public AmazonAuthority selectByGroupAndMarket(String groupid,String marketplaceid) {
		if(marketplaceid!=null&&marketplaceid.equals("EU")) {
			QueryWrapper<AmazonAuthority> queryWrapper=new QueryWrapper<AmazonAuthority>();
			queryWrapper.eq("groupid", groupid);
			queryWrapper.eq("disable", Boolean.FALSE);
			queryWrapper.eq("region", "EU");
			List<AmazonAuthority> list = this.baseMapper.selectList(queryWrapper);
			if(list!=null&&list.size()>0) {
				return list.get(0);
			}else {
				return null;
			}
		}else {
			return this.baseMapper.selectByGroupAndMarket(groupid, marketplaceid);
		}
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
			body.put("redirect_uri",apiBuildService.getRedirecturl());
			body.put("client_id",apiBuildService.getClientId());
			body.put("client_secret",apiBuildService.getClientSecret());
	        String response = HttpClientUtil.postUrl(ApiBuildService.endpoint, body,null,URLEncodedUtils.CONTENT_TYPE);
	        JSONObject json = GeneralUtil.getJsonObject(response);    
	        String refresh_token = json.getString("refresh_token");
	        return refresh_token;
	}
	
	public Runnable refreshTokenByAuth(IAmazonAuthorityService self,AmazonAuthority auth) {
	   	 Runnable runnable = new Runnable() {
 			public void run() { 
		          	List<Marketplace> marketlist = iMarketplaceService.findbyauth(auth.getId());
		          	auth.setUseApi("getAuthorizationApiGrantless");
		          	AuthorizationApi api = apiBuildService.getAuthorizationApiGrantless(auth);
		          	String sellingPartnerId=auth.getSellerid();
		          	String developerId=marketlist.get(0).getDevAccountNum();
		          	String mwsAuthToken=auth.getMwsauthtoken();
		       		try {
		       			developerId=developerId.replace("-", "");
		       			developerId=developerId.replace("\n", "");
		  				GetAuthorizationCodeResponse codeResult = api.getAuthorizationCode(sellingPartnerId,developerId, mwsAuthToken);
		  				AuthorizationCode authCode = codeResult.getPayload();
		  				String code=authCode.getAuthorizationCode();
		  			    auth.setRefreshToken(getRefreshTokenByCode(code));
		  			    auth.setRefreshTokenTime(new Date());
		  			    self.updateById(auth);
		  			} catch (ApiException e) {
		  				// TODO Auto-generated catch block
		  				auth.setApiRateLimit(null, e);
		  				e.printStackTrace();
		  			} catch (HttpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 			}
  		};
       return runnable;
	}
	
	public void getRefreshTokenByRegion(String region) {
        List<AmazonAuthority> authlist = this.baseMapper.selectByRegion(region);
    	List<Runnable> runlist=new ArrayList<Runnable>();
        for(AmazonAuthority auth:authlist) {
        	if(auth.getRefreshToken()!=null) {
       		     continue;
	       	}else {
	       		auth.setRefreshToken(region);
	       	}
        	Runnable runnable = refreshTokenByAuth(this,auth);
        	runlist.add(runnable);
        }
		executThread(runlist,"RefreshTokenByRegion"+region);
	}
	
	public List<String> getAllRegion() {
		List<String> region=new ArrayList<String>();
		region.add(Regions.EU_WEST_1.getName());
		region.add(Regions.US_EAST_1.getName());
		region.add(Regions.US_WEST_2.getName());
		return region;
	}
	
	public 	List<AmazonAuthority> getAllAuth(String region) {
		return this.baseMapper.getAvailableAuthority(region);
	}
	
	public 	List<AmazonAuthority> getAllAuth() {
		return this.baseMapper.getAvailableAuthority(null);
	}
	
	public void executTask(IRunAmazonService apiService) {
	    List<String> regions = getAllRegion() ;
		List<Runnable> runlist=new ArrayList<Runnable>();
		for(String region:regions) { 
			   List<AmazonAuthority> list = getAllAuth(region);
			      for(AmazonAuthority auth:list) { 
			    	  runlist.add(getRunnable(apiService,auth));
			      }
		}
		String type=apiService.toString().split("@")[0];
		executThread(runlist,type);
	}
	
	public  Runnable getRunnable(IRunAmazonService apiService,AmazonAuthority auth) {
		return new Runnable() {
			public void run() { 
				  apiService.runApi(auth);
			}
		};
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

 
	ConcurrentHashMap<String,Integer> runMap=new ConcurrentHashMap<String,Integer>();
	public   void executThread(List<Runnable> runnables, String type) {
		Integer numberObject=runMap.get(type);
		Integer runnumber=0;
		if(numberObject!=null) {
			runnumber=numberObject;
		}
		runMap.put(type, runnumber+1);
		if (runnables == null||(runnumber>0&&runnumber<5)){
			return;
		}
			// TODO Auto-generated method stub
			List<Future<?>> threadList = new ArrayList<Future<?>>();
			for (int i = 0; i < runnables.size(); i++) {
				if (runnables.get(i) != null) {
					threadList.add(threadPoolTaskExecutor.submit(runnables.get(i)));
				}
			}
			if(type!=null&&type.equals("anlyway")) {
		    	return ;
		    }
			new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
							int time = 1;
							boolean alldone = false;
							while (alldone == false && time < 20) {
								alldone = true;
								for (int i = 0; i < threadList.size(); i++) {
									if (!threadList.get(i).isDone()) {
										alldone = false;
										break;
									}
								}
								try {
									Thread.sleep(6000);
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
					}catch(Exception e) {
						e.printStackTrace();
					}finally {
						runMap.put(type, 0);
					}
					
				}
				
			}).start();
			
		
	}

	@Override
	public List<AmazonAuthority> selectByMarket(String marketplaceid) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectByMarket(marketplaceid);
	}
	
	@Override
	public List<AmazonAuthority> selectByShopAndMarket(String shopid,String marketplaceid) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectByShopAndMarket(shopid,marketplaceid);
	}
	
	

	@Override
	public AmazonAuthority selectBySellerId(String sellerid) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectBySellerId(sellerid);
	}
	
	@Override
	@CacheEvict(value = { "AmazonAuthorityCache" }, allEntries = true)
	public AmazonAuthority saveAuth(AmazonAuthority auth) {
		// TODO Auto-generated method stub
		AmazonAuthority oldAuth=null;
		oldAuth=this.selectBySellerId(auth.getSellerid());
		if(StrUtil.isBlank(auth.getSellerid())) {
			throw new BizException("sellerid 不能为空");
		}
		if(StrUtil.isBlank(auth.getAccessKeyId())) {
			throw new BizException("Access Key 不能为空");
		}
		if(StrUtil.isBlank(auth.getSecretKey())) {
			throw new BizException("Secret Key 不能为空");
		}
		if(StrUtil.isBlank(auth.getClientId())) {
			throw new BizException("Client Id 不能为空");
		}
		if(StrUtil.isBlank(auth.getClientSecret())) {
			throw new BizException("Client Secret 不能为空");
		}
		if(StrUtil.isBlank(auth.getRefreshToken())) {
			throw new BizException("Refresh Token 不能为空");
		}
		if(StrUtil.isBlank(auth.getRoleArn())) {
			throw new BizException("Role Arn 不能为空");
		}
		auth.setAccessKeyId(auth.getAccessKeyId().trim());
		auth.setSecretKey(auth.getSecretKey().trim());
		auth.setClientId(auth.getClientId().trim());
		auth.setClientSecret(auth.getClientSecret().trim()); 
		auth.setRefreshToken(auth.getRefreshToken().trim()); 
		auth.setSellerid(auth.getSellerid().trim());
		auth.setRoleArn(auth.getRoleArn().trim());
		AmazonGroup group = this.iAmazonGroupService.getById(auth.getGroupid());
		if(group==null) {
			throw new BizException("未找到对应店铺");
		}
		if(oldAuth!=null) {
			auth.setId(oldAuth.getId());
			auth.setCreatetime(oldAuth.getCreatetime());
			auth.setRefreshTokenTime(new Date());
			auth.setOpttime(new Date());
			auth.setDisable(false);
			auth.setStatus(null);
			auth.setStatusupdate(null);
			this.updateById(auth);
			AmazonAuthority newauth = this.getById(oldAuth.getId());
			iAmazonSellerMarketService.refreshMarketByAuth(newauth);
			return auth;
		}else {
			auth.setRefreshTokenTime(new Date());
			auth.setOpttime(new Date());
			auth.setCreatetime(new Date());
			auth.setDisable(false);
			auth.setStatus(null);
			auth.setStatusupdate(null);
			this.save(auth);
			AmazonAuthority newauth = this.getById(auth.getId());
			iAmazonSellerMarketService.refreshMarketByAuth(newauth);
			Calendar cstart = Calendar.getInstance();
			cstart.add(Calendar.DATE, -30);
			Calendar cend = Calendar.getInstance();
			handlerReportService.requestReport(auth.getId(),ReportType.OrdersByOrderDateReport,cstart,cend,false); 
			handlerReportService.requestAuthReport("id",auth.getId(),ReportType.ProductListings,false); 
			handlerReportService.requestAuthReport("id",auth.getId(),ReportType.InventoryReport,false); 
			return auth;
		}
	}

	
	@Override
	@CacheEvict(value = { "AmazonAuthorityCache" }, allEntries = true)
	public AmazonAuthority authSeller(String state, String selling_partner_id, String mws_auth_token,
			String spapi_oauth_code) {
		// TODO Auto-generated method stub
		AmazonAuthority auth=new AmazonAuthority();
		auth.setSellerid(selling_partner_id);
		auth.setMwsauthtoken(mws_auth_token);
		String[] groupRegion = state.split("@");
		auth.setGroupid(groupRegion[0]);
		auth.setAWSRegion(groupRegion[1]);
		auth.setRegion(apiBuildService.getRegion(auth.getAWSRegion()));
		AmazonGroup group = this.iAmazonGroupService.getById(auth.getGroupid());
		if(group==null) {
			throw new BizException("未找到对应店铺");
		}
		auth.setShopId(group.getShopid());
		try {
			String refreshcode = this.getRefreshTokenByCode(spapi_oauth_code);
			auth.setRefreshToken(refreshcode);
			AmazonAuthority oldAuth=this.selectBySellerId(selling_partner_id);
			if(oldAuth!=null) {
				oldAuth.setGroupid(auth.getGroupid());
				oldAuth.setAWSRegion(auth.getAWSRegion());
				oldAuth.setMwsauthtoken(auth.getMwsauthtoken());
				oldAuth.setRefreshToken(auth.getRefreshToken());
				oldAuth.setRefreshTokenTime(new Date());
				oldAuth.setDisable(false);
				oldAuth.setOpttime(new Date());
				oldAuth.setStatus(null);
				oldAuth.setStatusupdate(null);
				this.updateById(oldAuth);
				iAmazonSellerMarketService.refreshMarketByAuth(auth);
				return oldAuth;
			}else {
				auth.setRefreshTokenTime(new Date());
				auth.setOpttime(new Date());
				auth.setCreatetime(new Date());
				auth.setDisable(false);
				auth.setStatus(null);
				auth.setStatusupdate(null);
				this.save(auth);
				iAmazonSellerMarketService.refreshMarketByAuth(auth);
				Calendar cstart = Calendar.getInstance();
				cstart.add(Calendar.DATE, -30);
				Calendar cend = Calendar.getInstance();
				handlerReportService.requestAuthReport("id",auth.getId(),ReportType.ProductListings,false); 
				handlerReportService.requestReport(auth.getId(),ReportType.OrdersByOrderDateReport,cstart,cend,false); 
				handlerReportService.requestAuthReport("id",auth.getId(),ReportType.InventoryReport,false); 
				return auth;
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e!=null&&e.getMessage()!=null) {
				throw new BizException("授权异常:"+e.getMessage());
			}else {
				throw new BizException("授权异常");
			}
		}
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
    	result+="&redirect_uri="+apiBuildService.getRedirecturl();
		return result;
	}

	@Override
	@CacheEvict(value = { "AWSmarketplaceListCache", "ApiCache","AmazonAuthorityCache" }, allEntries = true)
	public boolean updateById(AmazonAuthority entity) {
		// TODO Auto-generated method stub
		return super.updateById(entity);
	}

	@Override
	public AmazonAuthMarketPerformance getPerformance(AmazonAuthority auth, String marketplaceid) {
		QueryWrapper<AmazonAuthMarketPerformance> query= new QueryWrapper<AmazonAuthMarketPerformance>();
		query.eq("amazonauthid", auth.getId());
		query.eq("marketplaceid", marketplaceid);
		AmazonAuthMarketPerformance perf = amazonAuthMarketPerformanceMapper.selectOne(query);
		return perf;
	}
   

	@Override
	public List<AmazonGroupVO> selectBindAuth(UserInfo user) {
		String shopid=user.getCompanyid();
		QueryWrapper<AmazonGroup> queryWrapper=new QueryWrapper<AmazonGroup>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("isdelete", false);
		queryWrapper.orderByAsc("findex");
		List<AmazonGroup> grouplist=iAmazonGroupService.list(queryWrapper);
		List<AmazonGroupVO> result=new ArrayList<AmazonGroupVO>();
		Map<String, Marketplace> marketMap = iMarketplaceService.findMapByMarketplaceId();
		if(grouplist!=null&&grouplist.size()>0) {
           for(AmazonGroup groupitem:grouplist) {
        	   List<AmazonAuthority> authlist = this.selectByGroupId(groupitem.getId());
        	   List<Map<String,Object>> authAdvlist = this.baseMapper.selectAdvByGroupId(groupitem.getId());
        	   Map<String,AmazonRegionVO> regions=new HashMap<String,AmazonRegionVO>(); 
        	   for(AmazonAuthority authItem:authlist) {
                   String regionName="";
                   String regionCode="";
        		   //
        		   Map<String,AmazonMarketVO> countrys=new HashMap<String,AmazonMarketVO>();
        		   QueryWrapper<AmazonSellerMarket> querymarket =new QueryWrapper<AmazonSellerMarket>();
        		   querymarket.eq("sellerid", authItem.getSellerid());
        		   querymarket.eq("disable", false);
				   List<AmazonSellerMarket> marketlist =iAmazonSellerMarketService.list(querymarket);
				   for(AmazonSellerMarket marketItem:marketlist) {
					   AmazonMarketVO country = new AmazonMarketVO();
					   Marketplace marketplace = marketMap.get(marketItem.getMarketplaceId());
					   if(marketplace==null)continue;
					   country.setCountrycode(marketplace.getMarket());
					   country.setCountryname(marketplace.getName());
					   regionName=marketplace.getRegionName();
					   country.setMarketplaceid(marketItem.getMarketplaceId());
					   countrys.put(marketItem.getMarketplaceId()+authItem.getSellerid(), country);
				   }
        		    if(countrys.size()>0) {
        				AmazonRegionVO region=new AmazonRegionVO();
        				region.setAuthid(authItem.getId());
        				region.setCountrys(countrys);
        				region.setRegion(regionCode);
        				if(authItem.getStatus()==null) {
        					region.setSellerauth("1");
        				}else {
        					region.setSellerauth("0");
        				}
        				region.setRegionname(regionName);
        				region.setSellerid(authItem.getSellerid());
        				region.setTime(authItem.getRefreshTokenTime());
        				region.setAdauth("2");
        				region.setAdvid(null);
        		    	regions.put(authItem.getSellerid(),region);
        		    }
        	   }
         	   for(Map<String,Object> authAdvItem:authAdvlist) {
         		   String key=null;
         		   if(authAdvItem.get("region")==null) {
         			   continue;
         		   }
         		   key=authAdvItem.get("region").toString();
         		   String regionName=null;
         		   if(key.equals("NA")) {
         			   key="us-east-1";
         		   }else if(key.equals("EU")) {
         			   key="eu-west-1";
         		   }else {
         			   key="us-west-2";
         		   }
         		  AmazonRegionVO region=null;
         		
         		   String sellerid=null;
         		   List<Map<String, Object>> advmarketList = this.baseMapper.selectAdvMarketByAdvAuthId(authAdvItem.get("id").toString());
       		       for(Map<String, Object> advcountry:advmarketList) {
       		    	   String marketplaceid=advcountry.get("marketplaceId").toString();
       		    	   String countrykey=advcountry.get("marketplaceId").toString()+advcountry.get("sellerId").toString();
       		    	   sellerid=advcountry.get("sellerId").toString();
       		    	   Marketplace marketplace = marketMap.get(marketplaceid);
       		    	   regionName=marketplace.getRegionName();
       		    	     AmazonMarketVO country =null;
       		    	     region=regions.get(sellerid);
       		    	    	 if(region!=null) {
    	          			     region.setAdvid(authAdvItem.get("id").toString());
    	          				 region.setAdauth(authAdvItem.get("adauth").toString());
    	          		     }else {
    	          			    region=new AmazonRegionVO();
    	        				region.setRegion(authAdvItem.get("region").toString());
    	        				region.setSellerauth("2");
    	        				region.setRegionname(null);
    	        				region.setSellerid(null);
    	        			    region.setAdvid(authAdvItem.get("id").toString());
    	  				        region.setAdauth(authAdvItem.get("adauth").toString());
    	        				region.setTime(AmzDateUtils.getDate(authAdvItem.get("opttime")));
    	        				regions.put(sellerid, region);
    	          		     }
       		    	   
	       		    	 
       		    	   if(region.getCountrys()!=null) {
       		    		    country =region.getCountrys().get(countrykey);
         		    	  }
       		    	     if(country==null) {
       		    	    	country=new AmazonMarketVO();
  					        country.setCountrycode(marketplace.getMarket());
  					        country.setCountryname(marketplace.getName());
  					        country.setMarketplaceid(countrykey);
  					        country.setSellerid(advcountry.get("sellerId").toString());
  					        if(region.getCountrys()!=null) {
  					        	region.getCountrys().put(countrykey, country);
  					        }else {
  					        	Map<String,AmazonMarketVO> countrys=new HashMap<String,AmazonMarketVO>();
  					        	countrys.put(countrykey, country);
  					        	region.setCountrys(countrys);
  					        }
       		    	     } 
       		       }
       		      if(region!=null&&region.getSellerid()==null) {
       		    	region.setSellerid(sellerid);
       		      }
       		      if(region!=null&&region.getRegionname()==null) {
       		    	  region.setRegionname(regionName);
       		      }
         		  
       	        }
        	   if(regions!=null&&regions.size()>0) {
        		   AmazonGroupVO groupvo=new AmazonGroupVO();
        		   groupvo.setGname(groupitem.getName());
        		   groupvo.setGroupid(groupitem.getId());
        		   groupvo.setRegions(regions);
        		   result.add(groupvo);
        	   }
           }
		 }
		return result;
	}


	@Override
	@CacheEvict(value = { "AmazonAuthorityCache" }, allEntries = true)
	public int deleteByLogic(Map<String, Object> param) {
		int result=0;
		QueryWrapper<AmazonSellerMarket> queryWrapper=new QueryWrapper<AmazonSellerMarket>();
		queryWrapper.eq("sellerid", param.get("sellerid").toString());
		List<AmazonSellerMarket> list = iAmazonSellerMarketService.list(queryWrapper);
		if(list!=null && list.size()>0) {
			for(AmazonSellerMarket item:list) {
				param.put("sellerid", item.getSellerid());
				param.put("marketplaceId", item.getMarketplaceId());
				result+=this.baseMapper.deleteByLogic(param);
			}
		}
		return result;
	}

	@Override
	public AmazonAuthority selectByGroupAndRegion(String groupid, String region) {
		// TODO Auto-generated method stub
		QueryWrapper<AmazonAuthority> queryWrapper=new QueryWrapper<AmazonAuthority>();
		queryWrapper.eq("groupid", groupid);
		queryWrapper.eq("region", region);
		queryWrapper.eq("disable", Boolean.FALSE);
		return this.baseMapper.selectOne(queryWrapper);
	}

	@Override
	@Cacheable(value = "AmazonAuthorityCache#6000")
	public AmazonAuthority getById(Serializable id) {
		// TODO Auto-generated method stub
		return super.getById(id);
	}

	@Override
	public List<AmazonAuthority> selectByshopid(String companyid) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectByshopid(companyid);
	}
	
	
	@Override
	public List<Map<String,Object>> selectAuthMaketplace(String shopid) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectAuthMaketplace(shopid);
	}

	@Override
	public List<String> getAllBossEmalShop() {
		// TODO Auto-generated method stub
		return this.baseMapper.getAllBossEmalShop();
	}

 
	


}