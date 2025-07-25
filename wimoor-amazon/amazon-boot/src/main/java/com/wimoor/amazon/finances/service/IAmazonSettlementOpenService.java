package com.wimoor.amazon.finances.service;

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.client.ApiException;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.finances.pojo.entity.AmzFinAccount;

public interface IAmazonSettlementOpenService {
	public void getGroupIdData() throws ApiException, InterruptedException, LWAException;
	public void getGroupIdData(AmazonAuthority amazonAuthority) throws ApiException, InterruptedException, LWAException;
	public void getGroupIdData(AmazonAuthority amazonAuthority,AmzFinAccount item) throws ApiException, InterruptedException, LWAException ;
}
