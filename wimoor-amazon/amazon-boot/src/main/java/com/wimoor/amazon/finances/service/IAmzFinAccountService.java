package com.wimoor.amazon.finances.service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.finances.pojo.entity.AmzFinAccount;

import java.util.Map;

import com.amazon.spapi.model.finances.ListFinancialEventGroupsResponse;
import com.amazon.spapi.model.finances.ListFinancialEventsResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
public interface IAmzFinAccountService extends IService<AmzFinAccount> ,IRunAmazonService{

	void handler(AmazonAuthority amazonAuthority, ListFinancialEventGroupsResponse result);
	public Map<String, Map<String, Object>> getFinancialByShopId(String shopid, String currency) ;
	public IPage<Map<String, Object>> getFinancial(Page<Map<String, Object>> page, Map<String, Object> map);
	Map<String, Object> getFinancialSum(Map<String, Object> map);
	Object getFaccDetial(String shopid, String amazonauthid, String accgroupid, String nextToken);
	public ListFinancialEventsResponse listFinancialEventsByGroupId(AmazonAuthority amazonAuthority,String groupid,String token) ;
}
