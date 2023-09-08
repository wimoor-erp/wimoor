package com.wimoor.amazon.auth.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.vo.AmazonGroupVO;
import com.wimoor.amazon.report.pojo.entity.AmazonAuthMarketPerformance;
import com.wimoor.common.user.UserInfo;
 
public interface IAmazonAuthorityService  extends IService<AmazonAuthority> {

	List<AmazonAuthority> selectByMarket(String marketplaceid);

	public   void executTask(IRunAmazonService apiService) ;

	public   void executThread(List<Runnable> runnables,String type) ;
	
	public AmazonAuthority selectByGroupAndMarket(String groupid,String marketplaceid);
	
	public AmazonAuthority selectBySellerId(String sellerid);
	
	public void getRefreshTokenByRegion(String region);

	AmazonAuthority authSeller(String state, String selling_partner_id, String mws_auth_token, String spapi_oauth_code);

	public String getAuthUrl(String groupid, String marketplaceid);
	
	public List<AmazonAuthority> selectByGroupId(String groupid);
	
	public 	List<AmazonAuthority> getAllAuth(String region) ;
	
	public 	List<AmazonAuthority> getAllAuth() ;

	AmazonAuthMarketPerformance getPerformance(AmazonAuthority auth, String marketplaceid);
	
	public List<AmazonGroupVO> selectBindAuth(UserInfo user);
	
	public int deleteByLogic(Map<String,Object> param);

	AmazonAuthority selectByGroupAndRegion(String groupid, String region);

	List<AmazonAuthority> selectByShopAndMarket(String shopid, String marketplaceid);

	List<AmazonAuthority> selectByshopid(String companyid);

	AmazonAuthority saveAuth(AmazonAuthority auth);

	List<Map<String, Object>> selectAuthMaketplace(String shopid);

	List<String> getAllBossEmalShop();

}
