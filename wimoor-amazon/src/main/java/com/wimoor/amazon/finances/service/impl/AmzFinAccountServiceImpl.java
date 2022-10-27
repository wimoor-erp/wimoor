package com.wimoor.amazon.finances.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;

import com.amazon.spapi.api.FinancesApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.finances.Currency;
import com.amazon.spapi.model.finances.FinancialEventGroup;
import com.amazon.spapi.model.finances.FinancialEventGroupList;
import com.amazon.spapi.model.finances.FinancialEvents;
import com.amazon.spapi.model.finances.ListFinancialEventGroupsPayload;
import com.amazon.spapi.model.finances.ListFinancialEventGroupsResponse;
import com.amazon.spapi.model.finances.ListFinancialEventsResponse;
import com.amazon.spapi.model.finances.ShipmentEvent;
import com.amazon.spapi.model.finances.ShipmentEventList;
import com.amazon.spapi.model.finances.ShipmentItem;
import com.amazon.spapi.model.finances.ShipmentItemList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmzAuthApiTimelimit;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.finances.mapper.AmzFinAccountMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzFinAccount;
import com.wimoor.amazon.finances.pojo.entity.OrdersFinancial;
import com.wimoor.amazon.finances.service.IAmzFinAccountService;
import com.wimoor.amazon.finances.service.IOrdersFinancialService;
import com.wimoor.amazon.util.AmzDateUtils;
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
					c.set(Calendar.DATE, -30);
					ApiCallback<ListFinancialEventGroupsResponse> callback=new ApiCallbackListFinancialEventGroupsEvents(this,amazonAuthority);;
					OffsetDateTime financialEventGroupStartedAfter=AmzDateUtils.getOffsetDateTimeUTC(c);
					api.listFinancialEventGroupsAsync(100, null, financialEventGroupStartedAfter,null, callback);
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
			ApiCallback<ListFinancialEventGroupsResponse> callback=new ApiCallbackListFinancialEventGroupsEvents(this,amazonAuthority);;
			api.listFinancialEventGroupsAsync(100, null, null,nexttoken, callback);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			amazonAuthority.setApiRateLimit( null, e);
		}
	}

	
	@Override
	public void handler(AmazonAuthority amazonAuthority, ListFinancialEventGroupsResponse response) {
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
			fin.setProcessingStatus(item.getProcessingStatus());
			if(oldfin!=null) {
				this.baseMapper.update(fin, query);
			}else {
				this.baseMapper.insert(fin);
			}
		}
		if(response.getPayload().getNextToken()!=null) {
			runApiNextToken(amazonAuthority,response.getPayload().getNextToken());
		}
	}

}
