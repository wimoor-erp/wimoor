package com.wimoor.amazon.adv.report.service.impl;


import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.wimoor.amazon.adv.common.pojo.CampaignType;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.report.dao.AmzAdvSumAllTypeMapper;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllTypeKey;
import com.wimoor.amazon.adv.report.service.IAmzAdvSumAllTypeService;
import com.wimoor.amazon.adv.report.service.impl.AmzAdvReportHandlerServiceImpl.AdvResponseRecordType;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvSumAllTypeService")
public class AmzAdvSumAllTypeServiceImpl extends BaseService<AmzAdvSumAllType> implements IAmzAdvSumAllTypeService {
@Resource
AmzAdvSumAllTypeMapper amzAdvSumAllTypeMapper;

public  int updateByKey(AmzAdvSumAllType entity) {
	// TODO Auto-generated method stub
	 return amzAdvSumAllTypeMapper.updateByKey(entity);
}

public int insert(AmzAdvSumAllType entity) {
	// TODO Auto-generated method stub
	return amzAdvSumAllTypeMapper.insert(entity);
}

 
public AmzAdvSumAllType selectByKey(AmzAdvSumAllTypeKey key) {
	// TODO Auto-generated method stub
	return amzAdvSumAllTypeMapper.selectByKey(key);
}
	 
public void addTodaySPRecordType(BigInteger profileid,String recordType,int qty){
	AmzAdvSumAllType typesum=new AmzAdvSumAllType();
	typesum.setProfileid(profileid);
	typesum.setCampaigntype(CampaignType.sp);
	typesum.setRecordtype(recordType);
	typesum.setByday(new Date());
	typesum.setOpttime(new Date());
	typesum.setState(AdvState.enabled);
	typesum.setQuantity(qty);
	AmzAdvSumAllType old = amzAdvSumAllTypeMapper.selectByKey(typesum);
	if(old!=null) {
		old.setQuantity(old.getQuantity()+typesum.getQuantity());
		amzAdvSumAllTypeMapper.updateByKey(old);
	}
	else amzAdvSumAllTypeMapper.insert(typesum);
}

public Map<String, Object> getTypeNumber(String shopid) {
	// TODO Auto-generated method stub
	Map<String,Object> result=new HashMap<String,Object>(); 
	Calendar c =Calendar.getInstance();
    List<Map<String, Object>> list = amzAdvSumAllTypeMapper.getTypeNumberWithoutUnUseable(shopid,GeneralUtil.formatDate(c.getTime()));
    int i=0;
	while(i<3&&(list==null||list.size()==0)) {
		c.add(Calendar.DATE, -1);
		list=  amzAdvSumAllTypeMapper.getTypeNumberWithoutUnUseable(shopid,GeneralUtil.formatDate(c.getTime()));
		i++;
	}
    for(Map<String, Object> item:list) {
    	result.put(item.get("recordType").toString(), item.get("quantity"));
    }
	if(!result.containsKey(AdvResponseRecordType.adGroup)) {
    	result.put(AdvResponseRecordType.adGroup,0);
    }
    if(!result.containsKey(AdvResponseRecordType.campaign)) {
    	result.put(AdvResponseRecordType.campaign,0);
    }
    if(!result.containsKey(AdvResponseRecordType.campaignNegativeKeyword)) {
    	result.put(AdvResponseRecordType.campaignNegativeKeyword,0);
    }
    if(!result.containsKey(AdvResponseRecordType.keyword)) {
    	result.put(AdvResponseRecordType.keyword,0);
    }
    if(!result.containsKey(AdvResponseRecordType.negativeKeyword)) {
    	result.put(AdvResponseRecordType.negativeKeyword,0);
    }
    if(!result.containsKey(AdvResponseRecordType.productAd)) {
    	result.put(AdvResponseRecordType.productAd,0);
    }
    if(!result.containsKey(AdvResponseRecordType.target)) {
    	result.put(AdvResponseRecordType.target,0);
    }
    if(!result.containsKey("searchTerms")) {
    	result.put("searchTerms",0);
    }
    if(!result.containsKey("planTask")) {
    	result.put("planTask",0);
    }
    if(!result.containsKey("historyRecord")) {
    	result.put("historyRecord",0);
    }
    return result;
}
public Map<String, Object> getEnableNumber(String shopid) {
	// TODO Auto-generated method stub
	Map<String,Object> result=new HashMap<String,Object>(); 
	Calendar c =Calendar.getInstance();
	List<Map<String,Object>> list=  amzAdvSumAllTypeMapper.getTypeNumberEnabled(shopid,GeneralUtil.formatDate(c.getTime()));
    int i=0;
	while(i<3&&(list==null||list.size()==0)) {
		c.add(Calendar.DATE, -1);
		list=  amzAdvSumAllTypeMapper.getTypeNumberEnabled(shopid,GeneralUtil.formatDate(c.getTime()));
		i++;
	}
	for(Map<String, Object> item:list) {
    	result.put(item.get("recordType").toString(), item.get("quantity"));
    }
	if(!result.containsKey(AdvResponseRecordType.adGroup)) {
    	result.put(AdvResponseRecordType.adGroup,0);
    }
    if(!result.containsKey(AdvResponseRecordType.campaign)) {
    	result.put(AdvResponseRecordType.campaign,0);
    }
    if(!result.containsKey(AdvResponseRecordType.campaignNegativeKeyword)) {
    	result.put(AdvResponseRecordType.campaignNegativeKeyword,0);
    }
    if(!result.containsKey(AdvResponseRecordType.keyword)) {
    	result.put(AdvResponseRecordType.keyword,0);
    }
    if(!result.containsKey(AdvResponseRecordType.negativeKeyword)) {
    	result.put(AdvResponseRecordType.negativeKeyword,0);
    }
    if(!result.containsKey(AdvResponseRecordType.productAd)) {
    	result.put(AdvResponseRecordType.productAd,0);
    }
    if(!result.containsKey(AdvResponseRecordType.target)) {
    	result.put(AdvResponseRecordType.target,0);
    }
    return result;
}

public List<Map<String, Object>> getMonthSumAdgroupNum(Map<String, Object> param){
	param.put("recordType", AdvResponseRecordType.adGroup);
	return this.amzAdvSumAllTypeMapper.getMonthSumNum(param);
}

public List<Map<String, Object>> getMonthSumAsinNum(Map<String, Object> param){
	param.put("recordType", "asin");
	return this.amzAdvSumAllTypeMapper.getMonthSumNum(param);
}

	@Override
	public  Map<String, Object> getSumtypeNums(QueryForList dto) {
		if(!"adcams".equals(dto.getFtype()) && !"adgroups".equals(dto.getFtype()) && !"ProductAds".equals(dto.getFtype())){
			return null;
		}
		if(dto.getCampaignType().equals("SP")){
			List<Map<String, Object>> list = this.amzAdvSumAllTypeMapper.selectSPAdvSummaryNums(dto);
			Map<String,Object> maps=new HashMap<String,Object>();
			for(Map<String, Object> item:list) {
				maps.put(item.get("ftype").toString(), Integer.parseInt(item.get("nums").toString()));
			}
			return maps;
		}else if(dto.getCampaignType().equals("SB")){
			List<Map<String, Object>> list = this.amzAdvSumAllTypeMapper.selectSBAdvSummaryNums(dto);
			Map<String,Object> maps=new HashMap<String,Object>();
			for(Map<String, Object> item:list) {
				maps.put(item.get("ftype").toString(), Integer.parseInt(item.get("nums").toString()));
			}
			return maps;
		}else{
			List<Map<String, Object>> list = this.amzAdvSumAllTypeMapper.selectSDAdvSummaryNums(dto);
			Map<String,Object> maps=new HashMap<String,Object>();
			for(Map<String, Object> item:list) {
				maps.put(item.get("ftype").toString(), Integer.parseInt(item.get("nums").toString()));
			}
			return maps;
		}
	}
}
