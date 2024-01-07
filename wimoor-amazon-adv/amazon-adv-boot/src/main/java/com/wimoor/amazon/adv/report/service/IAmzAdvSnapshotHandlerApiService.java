package com.wimoor.amazon.adv.report.service;

import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;

public interface IAmzAdvSnapshotHandlerApiService {
	public void requestHsaCampaign(AmzAdvProfile profile) ;
	public void requestHsaKeyword(AmzAdvProfile profile);
	public void requestHsaAdgroups(AmzAdvProfile profile);
	public void requestSPCampProductTargeNegative(AmzAdvProfile profile);
	public void requestSDProductTargeNegative(AmzAdvProfile profile);
	public void requestHsaTarget(AmzAdvProfile profile);
	public void requestHsaAds(AmzAdvProfile profile);
	public void requestSPCampBudgetRule(AmzAdvProfile profile);
}
