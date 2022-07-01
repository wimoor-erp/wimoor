package com.wimoor.amazon.auth.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
 
public interface IAmazonAuthorityService  extends IService<AmazonAuthority> {

	List<AmazonAuthority> selectByMarket(String marketplaceid);

	public   void executTask(IRunAmazonService apiService) ;

	public   void executThread(List<Runnable> runnables, int waitMinute) ;
	
	public AmazonAuthority selectByGroupAndMarket(String groupid,String marketplaceid);
	
	public AmazonAuthority selectBySellerId(String sellerid);
	
	public void getRefreshTokenByRegion(String region);

	AmazonAuthority authSeller(String state, String selling_partner_id, String mws_auth_token, String spapi_oauth_code);

	String getAuthUrl(String groupid, String marketplaceid);
	
	public List<AmazonAuthority> selectByGroupId(String groupid);
	
	public 	List<AmazonAuthority> getAllAuth(String region) ;
	
	public 	List<AmazonAuthority> getAllAuth() ;
	
}
