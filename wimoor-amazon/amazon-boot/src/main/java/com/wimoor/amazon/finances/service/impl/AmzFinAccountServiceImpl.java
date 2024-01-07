package com.wimoor.amazon.finances.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import com.amazon.spapi.api.FinancesApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.finances.FinancialEventGroup;
import com.amazon.spapi.model.finances.FinancialEventGroupList;
import com.amazon.spapi.model.finances.ListFinancialEventGroupsPayload;
import com.amazon.spapi.model.finances.ListFinancialEventGroupsResponse;
import com.amazon.spapi.model.finances.ListFinancialEventsResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmzAuthApiTimelimit;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.finances.mapper.AmzFinAccountMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzFinAccount;
import com.wimoor.amazon.finances.service.IAmazonSettlementOpenService;
import com.wimoor.amazon.finances.service.IAmzFinAccountService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.amazon.util.AmzUtil;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
@Service
public class AmzFinAccountServiceImpl extends ServiceImpl<AmzFinAccountMapper, AmzFinAccount> implements IAmzFinAccountService {

	@Autowired
	ApiBuildService apiBuildService;
	@Autowired
	IMarketplaceService marketplaceService;
    @Autowired
    IExchangeRateHandlerService exchangeRateHandlerService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	@Lazy
	IAmazonSettlementOpenService iAmazonSettlementOpenService;
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("listFinancialEventGroups");
		FinancesApi api = apiBuildService.getFinancesApi(amazonAuthority);
		try {
			AmzAuthApiTimelimit limit = amazonAuthority.getApiRateLimit();
			if(limit.apiNotRateLimit()) {
				if(!StrUtil.isBlank(limit.getNexttoken())) {
					runApiNextToken(amazonAuthority,limit.getNexttoken());
				}else {
					Calendar c = Calendar.getInstance();
					OffsetDateTime financialEventGroupStartedBefore=null;
					c.set(Calendar.DATE, -120);
					OffsetDateTime financialEventGroupStartedAfter=AmzDateUtils.getOffsetDateTimeUTC(c);
					ApiCallback<ListFinancialEventGroupsResponse> callback=new ApiCallbackListFinancialEventGroupsEvents(this,amazonAuthority);
					api.listFinancialEventGroupsAsync(100,   financialEventGroupStartedBefore, financialEventGroupStartedAfter,null, callback);
				}
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			amazonAuthority.setApiRateLimit( null, e);
		}
	}
 
	public void runApiNextToken(AmazonAuthority amazonAuthority,String nexttoken) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("listFinancialEventGroups");
		FinancesApi api = apiBuildService.getFinancesApi(amazonAuthority);
		try {
			ApiCallback<ListFinancialEventGroupsResponse> callback=new ApiCallbackListFinancialEventGroupsEvents(this,amazonAuthority);
			api.listFinancialEventGroupsAsync(100, null, null,nexttoken, callback);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			amazonAuthority.setApiRateLimit( null, e);
		}
	}

	
	@Override
	public synchronized  void handler(AmazonAuthority amazonAuthority, ListFinancialEventGroupsResponse response) {
	    ListFinancialEventGroupsPayload result = response.getPayload();
	    FinancialEventGroupList list = result.getFinancialEventGroupList();
		// TODO Auto-generated method stub
		for(FinancialEventGroup item:list) {
			LambdaQueryWrapper<AmzFinAccount> query=new LambdaQueryWrapper<AmzFinAccount>()
			          .eq(AmzFinAccount::getAmazonAuthid, amazonAuthority.getId())
			          .eq(AmzFinAccount::getGroupid, item.getFinancialEventGroupId());
			AmzFinAccount oldfin=this.baseMapper.selectOne(query);
			AmzFinAccount fin=null;
			if(oldfin!=null) {
				fin=oldfin;
			}else {
				fin=new AmzFinAccount();
			}
			fin.setAmazonAuthid(new BigInteger(amazonAuthority.getId()));
			fin.setGroupid(item.getFinancialEventGroupId());
			fin.setFinancialEventGroupStart(AmzDateUtils.getLocalTime(item.getFinancialEventGroupStart()));
			fin.setFinancialEventGroupEnd(AmzDateUtils.getLocalTime(item.getFinancialEventGroupEnd()));
			fin.setFundTransferDate(AmzDateUtils.getLocalTime(item.getFundTransferDate()));
			if(item.getBeginningBalance()!=null) {
				fin.setBeginningBalance(item.getBeginningBalance().getCurrencyAmount());
				if(fin.getCurrency()==null) {
					fin.setCurrency(item.getBeginningBalance().getCurrencyCode());
				}
			}
			if(item.getConvertedTotal()!=null) {
				fin.setConvertedTotal(item.getConvertedTotal().getCurrencyAmount());
				if(fin.getCurrency()==null) {
					fin.setCurrency(item.getConvertedTotal().getCurrencyCode());
				}
			}
			fin.setAccountTail(item.getAccountTail());
			fin.setTraceId(item.getTraceId());
			fin.setFundTransferStatus(item.getFundTransferStatus());
			if(item.getOriginalTotal()!=null) {
				fin.setOriginalTotal(item.getOriginalTotal().getCurrencyAmount());
				if(fin.getCurrency()==null) {
					fin.setCurrency(item.getOriginalTotal().getCurrencyCode());
				}
			}
			fin.setRefreshtime(new Date());
			fin.setProcessingStatus(item.getProcessingStatus());
			if(fin.getFundTransferStatus()!=null&&fin.getFundTransferStatus().equals("Succeeded")) {
				fin.setNexttoken(null);
			}
			if(oldfin!=null) {
				this.baseMapper.update(fin, query);
			}else {
				this.baseMapper.insert(fin);
			}
	           final AmzFinAccount groupfin=fin;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
					 try {
						iAmazonSettlementOpenService.getGroupIdData(amazonAuthority, groupfin);
					} catch (ApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					
				}).start();
			
		}
		if(response.getPayload().getNextToken()!=null) {
			runApiNextToken(amazonAuthority,response.getPayload().getNextToken());
		}
	}

	
	public Map<String, Map<String, Object>> getFinancialByShopId(String shopid, String currency) {
		List<Map<String, Object>> list = this.baseMapper.selectUnclosedFinByShopid(shopid);
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> item : list) {
			String mcurrency = GeneralUtil.getStr(item.get("currency"));
			if (mcurrency == null) {
				mcurrency = currency;
			}
			String groupid = GeneralUtil.getStr(item.get("groupid"));
			BigDecimal omoney = item.get("original_total") == null ? new BigDecimal("0") : (BigDecimal) item.get("original_total");
			BigDecimal newmoney = exchangeRateHandlerService.changeCurrencyByLocal(mcurrency, currency, omoney);
			Map<String, Object> map = result.get(groupid);
			if (map == null) {
				map = new HashMap<String, Object>();
				map.put("groupname", item.get("name"));
				map.put("groupid", item.get("groupid"));
			}
			BigDecimal oldmoney = map.get("money") == null ? new BigDecimal("0") : (BigDecimal) map.get("money");
			BigDecimal money = oldmoney.add(newmoney);
			map.put("money", money);
			result.put(groupid, map);
		}
		return result;
	}

	@Override
	public IPage<Map<String, Object>> getFinancial(Page<Map<String, Object>> page, Map<String, Object> map) {
			String tocurrency = "RMB";
			String shopid = map.get("shopid").toString();
			if (map.get("currency") != null) {
				tocurrency = map.get("currency").toString();
			}
			String marketplace_name=(String) map.get("marketplace_name");
			List<Marketplace> marketplacelist = marketplaceService.findAllMarketplace();
			Map<String,String> currname=new HashMap<String,String>();
			String currency="";
			for(Marketplace item:marketplacelist) {
				if(item.getPointName().equals(marketplace_name)) {
					currency=item.getCurrency();
				}
				if("EUR".equals(item.getCurrency())) {
					currname.put(item.getCurrency(), "欧洲");
				}else {
					currname.put(item.getCurrency(), item.getName());
				}
			}
			currname.put("SEK", "瑞典");
			if(StrUtil.isEmpty(currency)) {
				map.put("currency", null);
			}else {
				map.put("currency", currency);
			}
			IPage<Map<String, Object>> list = this.baseMapper.getFinancial(page, map);
			if (list.getRecords().size() > 0 && list != null) {
				for (Map<String, Object> item : list.getRecords()) {
					if (item.get("currency") != null) {
						String fromCurrency = item.get("currency").toString();
						String market = currname.get(fromCurrency);
						item.put("marketName", market);
						if (item != null && item.get("totalAmount") != null) {
							BigDecimal amount = (BigDecimal) item.get("totalAmount");
							amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromCurrency, tocurrency, amount);
							item.put("totalAmount_to", amount.setScale(2, RoundingMode.HALF_UP));
						}
					}
				}
			}
			return list;
	}

	@Override
	public Map<String, Object> getFinancialSum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String,Object> result=new HashMap<String,Object>();
		String tocurrency = "RMB";
		String shopid = map.get("shopid").toString();
		if (map.get("currency") != null) {
			tocurrency = map.get("currency").toString();
		}
		result.put("currency", tocurrency);
		BigDecimal totalamount =new BigDecimal("0"); 
		BigDecimal totalFailedAmount=new BigDecimal("0"); 
		String marketplace_name=(String) map.get("marketplace_name");
		List<Marketplace> marketplacelist = marketplaceService.findAllMarketplace();
		Map<String,String> currname=new HashMap<String,String>();
		String currency="";
		for(Marketplace item:marketplacelist) {
			if(item.getPointName().equals(marketplace_name)) {
				currency=item.getCurrency();
			}
			if("EUR".equals(item.getCurrency())) {
				currname.put(item.getCurrency(), "欧洲");
			}else {
				currname.put(item.getCurrency(), item.getName());
			}
		}
		if(StrUtil.isEmpty(currency)) {
			map.put("currency", null);
		}else {
			map.put("currency", currency);
		}
		List<Map<String, Object>> list = this.baseMapper.getFinancialSum(map);
		if (list.size() > 0 && list != null) {
			for (Map<String, Object> item : list) {
				if (item.get("currency") != null) {
					String fromCurrency = item.get("currency").toString();
					if (item != null && item.get("totalAmount") != null) {
						BigDecimal amount = (BigDecimal) item.get("totalAmount");
						amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromCurrency, tocurrency, amount);
						item.put("totalAmount_to", amount.setScale(2,RoundingMode.HALF_UP));
						totalamount=totalamount.add(amount);
					}
					if (item != null && item.get("totalFailedAmount") != null) {
						BigDecimal amount = (BigDecimal) item.get("totalFailedAmount");
						amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromCurrency, tocurrency, amount);
						item.put("totalFailedAmount_to", amount.setScale(2, RoundingMode.HALF_UP));
						totalFailedAmount=totalFailedAmount.add(amount);
					}
				}
			}
		}
		result.put("acctotalsummary", totalamount);
		result.put("totalfailedsummary", totalFailedAmount);
		return result;
	}

	@Override
	public Object getFaccDetial(String shopid, String amazonauthid, String accgroupid, String nextToken) {
		// TODO Auto-generated method stub
		AmazonAuthority amazonAuthority = amazonAuthorityService.getById(amazonauthid);
		amazonAuthority.setUseApi("listFinancialEventGroups");
		FinancesApi api = apiBuildService.getFinancesApi(amazonAuthority);
		ListFinancialEventsResponse response;
		try {
			if(StrUtil.isBlank(nextToken)) {
				nextToken=null;
			}
			response = api.listFinancialEventsByGroupId(accgroupid, 100, nextToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AmzUtil.getException(e);
		}
	 
	    return response;
	}
	
	public ListFinancialEventsResponse listFinancialEventsByGroupId(AmazonAuthority amazonAuthority,String groupid,String token) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("listFinancialEventsByGroupId");
		FinancesApi api = apiBuildService.getFinancesApi(amazonAuthority);
		try {
			if(StrUtil.isBlankOrUndefined(token)) {
				 token=null;
			}
			AmzAuthApiTimelimit limit = amazonAuthority.getApiRateLimit();
			if(limit.apiNotRateLimit()) {
				ListFinancialEventsResponse res = api.listFinancialEventsByGroupId(groupid,100,token);
				return res;
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			amazonAuthority.setApiRateLimit( null, e);
		}
		return null;
	}
}
