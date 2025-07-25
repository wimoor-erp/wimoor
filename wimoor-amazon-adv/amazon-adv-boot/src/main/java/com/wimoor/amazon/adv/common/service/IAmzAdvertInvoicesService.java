package com.wimoor.amazon.adv.common.service;

import com.wimoor.amazon.adv.common.pojo.AmzAdvertInvoices;
import com.wimoor.common.user.UserInfo;

import java.util.List;
import java.util.Map;

public interface IAmzAdvertInvoicesService extends IService<AmzAdvertInvoices>{


    public String amzAdvInvoicesHandle();

    List<Map<String, Object>> getInvoicesSummary(UserInfo user,String groupid, String marketplaceid, String fromDate, String toDate);
}
