package com.wimoor.amazon.adv.report.service;



import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;

public interface IAmzAdvSnapshotHandlerService  {
 
	/**
	 * 获取快照
	 */
	public void requestSnapshot(String recordType,String campaignType);
	
	public int readSnapshot(String recordType,String campaignType);
	public void readSnapshot();
	public void requestSnapshotByProfile(AmzAdvProfile profiles,AmzAdvAuth advauth);	
	public void requestStoreBrand() ;
	public void requestStoreBrand(AmzAdvProfile amzAdvProfile);
	public void requestApiSnapshot(); 
	public void readSnapshot(String id);
}
