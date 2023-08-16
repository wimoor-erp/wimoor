package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.common.pojo.AmzRegion;
import com.wimoor.amazon.adv.common.pojo.Marketplace;

public interface IAmzAdvAuthService extends IService<AmzAdvAuth>{
	
	public List<Map<String, Object>> getProfileBySellerid(String sellerid);
	
	public List<Map<String, Object>> getAdvDateStatus(String sellerid, String status);
	
	public String captureProfiles(String groupid,String region);
	
	public void createProfiles(String groupid,String region,String country);
	
	public void createProfiles(String groupid,String region,String country,String brand);
	
	public List<AmzAdvProfile> getAmzAdvProfile(AmzAdvAuth advauth);	
	
	public AmzAdvProfile  getAmzAdvProfileByKey(BigInteger profileid);
	
	public AmzAdvAuth getAdvAuth(String groupid,String region);
	
	public AmzAdvAuth generateToken(String groupid,String region);
	
    public List<AmzRegion> getAllRegion();
    
	public Boolean captureAmzAdvAuthByCode(String userid,String code,String region,String groupid);
	
	public List<Map<String, Object>> getProfileByGroup(String groupid);
	
	public Map<String, Object> getProfileByGroupAndmarkplace(String groupid,String marketplaceid);
	
	public String amzAdvPost(AmzAdvProfile profile ,String url, String jsonparam); 
	
	public String amzAdvPost_V3(AmzAdvProfile profile ,String url, String jsonparam); 
	
	public String amzAdvAssetsPost(AmzAdvProfile profile ,String url, Map<String, Object> jsonparam);
	
	public String amzAdvGet(AmzAdvProfile profile ,String url);
	
	public String amzAdvGet_V3(AmzAdvProfile profile ,String url);
	
	public String amzAdvPut(AmzAdvProfile profile ,String url, String jsonparam);
	
	public String amzAdvPut_V3(AmzAdvProfile profile ,String url, String jsonparam);
	
	public String amzAdvDelete(AmzAdvProfile profile ,String url);
	
	public String amzAdvDelete_V3(AmzAdvProfile profile ,String url);
	
	public String amzAdvDownload(AmzAdvProfile profile ,String url);
	
	public boolean amzAdvDownloadFile(AmzAdvProfile profile ,AmzAdvRequest request,IAmzAdvHttpClientResponseHandler responseHandler);
	
	public List<Marketplace> getRegionByAdvGroup(String shopid, String advgroupid);
	
	public List<AmzAdvProfile> getProfileIdByGroup(String shopid, String advgroupid);
	
	public List<AmzAdvProfile> getProfileByAuth(String id);
	
	public List<Map<String,Object>> getSelleridBygroup(String groupid);
	
	public List<Map<String, Object>> findAmzAdvAuthWithDisable();
	
	public Map<String, Object> updateAmzAdvAuthForDisable(String id);
	
	public void deleteAmzAdvAuthDateWithDisable();
	
	public AmzAdvAuth getAuth(AmzAdvProfile profile);
	
	public AmzRegion getRegion(String region);
	
	Integer findBindAdvCount(String shopid);
	
	public int updateAdvAuthDisable(Map<String, Object> map);
	
	public int updateAdvAuthUndisable(String groupid, String region);
	
	public AmzRegion getClientIdByRegion(String region);
	
	public List<Map<String, Object>> getNoBindAdvRegion(String shopid, String groupid);
	
	public AmzAdvAuth getAdvAuthDisable(String groupid, String region);
	
	public String amzAdvDownloadSD(AmzAdvProfile profile, String url, String snid);

	public String amzAdvPost_V3(AmzAdvProfile profile, String url, String string, String contentType);
	
	public String amzAdvGet_V3(AmzAdvProfile profile, String url,String contentType);

	public void setBusyTime(Date date);

	public boolean isBusy();
	public String getRedirecturl() ;
}
