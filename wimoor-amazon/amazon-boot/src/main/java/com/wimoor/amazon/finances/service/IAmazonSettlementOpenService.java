package com.wimoor.amazon.finances.service;

import com.amazon.spapi.client.ApiException;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.finances.pojo.entity.AmzFinAccount;

public interface IAmazonSettlementOpenService {
	public void getGroupIdData() throws ApiException, InterruptedException;
	public void getGroupIdData(AmazonAuthority amazonAuthority) throws ApiException, InterruptedException;
	public void getGroupIdData(AmazonAuthority amazonAuthority,AmzFinAccount item) throws ApiException, InterruptedException ;
}
