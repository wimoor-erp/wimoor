package com.wimoor.amazon.profit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.auth.mapper.AmazonGroupMapper;
import com.wimoor.amazon.auth.mapper.MarketplaceMapper;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.profit.mapper.InplaceFeeMapper;
import com.wimoor.amazon.profit.mapper.InventoryStorageFeeMapper;
import com.wimoor.amazon.profit.mapper.ManualProcessingFeeMapper;
import com.wimoor.amazon.profit.mapper.ProfitConfigCountryMapper;
import com.wimoor.amazon.profit.pojo.entity.InplaceFee;
import com.wimoor.amazon.profit.pojo.entity.InventoryStorageFee;
import com.wimoor.amazon.profit.pojo.entity.ManualProcessingFee;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitParameterService;
import com.wimoor.common.user.UserInfo;

@Service("profitParameterService")
public class ProfitParameterServiceImpl implements IProfitParameterService {
 
 
	@Resource
	ProfitConfigCountryMapper profitConfigCountryMapper;
	@Resource
	MarketplaceMapper marketplaceMapper;
	@Resource
	InplaceFeeMapper inplacefeeMapper;
	@Resource
	ManualProcessingFeeMapper manualProcessingFeeMapper;
	@Resource
	InventoryStorageFeeMapper inventoryStorageFeeMapper;
	@Resource
	AmazonGroupMapper amazonGroupMapper;
	@Resource
	IProfitCfgService profitCfgService;
	@Resource
	IAmazonGroupService amazonGroupService;
 
 
	public Map<String, String> findInplacefee(String country) {
		Map<String, String> inplacefeeMap = new HashMap<String, String>();
		List<InplaceFee> inplacefees = inplacefeeMapper.findByCountry(country);
		for (int i = 0; i < inplacefees.size(); i++) {
			InplaceFee inplacefee = inplacefees.get(i);
			inplacefeeMap.put(inplacefee.getId(), inplacefee.getName());
		}
		return inplacefeeMap;
	}

	public Map<String, BigDecimal> findManualProcessingFee() {
		Map<String, BigDecimal> manualProcessingFeeMap = new HashMap<String, BigDecimal>();
		List<ManualProcessingFee> manualProcessingFees = manualProcessingFeeMapper.findByCountry("US");
		for (int i = 0; i < manualProcessingFees.size(); i++) {
			ManualProcessingFee manualProcessingFee = manualProcessingFees.get(i);
			manualProcessingFeeMap.put(manualProcessingFee.getMonth(), manualProcessingFee.getManualprocessingfee());
		}
		return manualProcessingFeeMap;
	}

	public Map<String, String> findPointNameByRegion(String region) {
		Map<String, String> map = new HashMap<String, String>();
		List<Marketplace> marketplaceList = marketplaceMapper.findbyRegion(region);
		if (marketplaceList != null && marketplaceList.size() > 0) {
			for (int i = 0; i < marketplaceList.size(); i++) {
				map.put(marketplaceList.get(i).getMarketplaceid(), marketplaceList.get(i).getPointName());
			}
		} else {
			return null;
		}
		return map;
	}

	public List<BigDecimal> getStorageFeeMap(String country) {
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		List<InventoryStorageFee> invStorageFeeList = inventoryStorageFeeMapper
				.getInventoryStorageFeeByCountry(country);
		if (invStorageFeeList != null && invStorageFeeList.size() > 0) {
			for (int i = 0; i < invStorageFeeList.size(); i++) {
				InventoryStorageFee invStorageFee = invStorageFeeList.get(i);
				list.add(invStorageFee.getPrice());
			}
		} else {
			list = null;
		}
		return list;
	}

	  

	public Map<String, String> findNoBindPointNameByGroup(UserInfo user,String groupid) {
		Map<String, String> map = new HashMap<String, String>();
		List<Marketplace> allMarketplaceList =marketplaceMapper.selectMarketSupportBind();
		List<Marketplace> bindMarketplaceList = marketplaceMapper.selectMarketByGroupid(user.getCompanyid(),groupid);
		Set<String> bindMarketSet=new HashSet<String>();
		for (int i = 0; i < bindMarketplaceList.size(); i++) {
			bindMarketSet.add(bindMarketplaceList.get(i).getMarketplaceid());
		}
		if (allMarketplaceList != null && allMarketplaceList.size() > 0) {
			for (int i = 0; i < allMarketplaceList.size(); i++) {
				Marketplace market = allMarketplaceList.get(i);
				if(!bindMarketSet.contains(market.getMarketplaceid()))
				map.put(market.getMarketplaceid(), market.getPointName()+"("+market.getName()+")");
			}
		} else {
			return null;
		}
		return map;
	}

}
