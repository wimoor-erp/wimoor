package com.wimoor.amazon.adv.common.service.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.dao.ProductAdvertReportMapper;
import com.wimoor.amazon.adv.common.dao.ProductAdvertReportSummaryMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.ProductAdvertReport;
import com.wimoor.amazon.adv.common.pojo.ProductAdvertReportSummary;
import com.wimoor.amazon.adv.common.pojo.SummaryObject;
import com.wimoor.amazon.adv.common.service.IAmazonReportAdvSummaryService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("amazonReportAdvSummaryService")
public class AmazonReportAdvSummaryServiceImpl extends BaseService<ProductAdvertReportSummary>
		implements IAmazonReportAdvSummaryService {
	@Resource
	private ProductAdvertReportSummaryMapper productAdvertReportSummaryMapper;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ProductAdvertReportMapper productAdvertReportMapper;
	
	Map<String, Map<String, SummaryObject>> summarymap = null;

	public void addAdvReportSummary(ProductAdvertReport padv) {
		Map<String, SummaryObject> mainmap = summarymap.get(padv.getMainKey());
		if (mainmap == null) {
			mainmap = new HashMap<String, SummaryObject>();
			summarymap.put(padv.getMainKey(), mainmap);
		}
		SummaryObject summaryobject = mainmap.get(padv.getSubKey());
		if (summaryobject == null) {
			summaryobject = new SummaryObject();
			mainmap.put(padv.getSubKey(), summaryobject);
		}
		Integer count = 0;
		if (summaryobject.getCount() == null) {
			count = 0;
		} else {
			count = summaryobject.getCount();
		}
		summaryobject.setAsin(padv.getAsin());
		summaryobject.setClicks(summaryobject.getClicks() + padv.getClicks());
		summaryobject.setAcos(summaryobject.getAcos() + padv.getAcos());
		summaryobject.setCpc(summaryobject.getCpc().add(padv.getCpc()));
		summaryobject.setCtr(summaryobject.getCtr() + padv.getCtr());
		summaryobject.setCurrency(padv.getCurrency());
		summaryobject.setImpressions(summaryobject.getImpressions() + padv.getImpressions());
		summaryobject.setOrders(summaryobject.getOrders() + padv.getOrders());
		summaryobject.setRoas(summaryobject.getRoas() + padv.getRoas());
		summaryobject.setSpc(summaryobject.getSpc() + padv.getSpc());
		summaryobject.setSpend(summaryobject.getSpend().add(padv.getSpend()));
		summaryobject.setTotalsales(summaryobject.getTotalsales().add(padv.getTotalsales()));
		summaryobject.setUnits(summaryobject.getUnits() + padv.getUnits());
		summaryobject.setCount(count += 1);
	}

	public void confirm() {
		confirm(this.summarymap,"sp");
	}

	public void confirm(Map<String, Map<String, SummaryObject>> summap,String ctype) {
		Calendar c = Calendar.getInstance();
		String sellerid = "";
		String marketplaceid = "";
		for (Entry<String, Map<String, SummaryObject>> mainmap : summap.entrySet()) {
			String key = mainmap.getKey();
			Map<String, Object> mainKeyMap = ProductAdvertReport.getMainKeyItem(key);
			sellerid = mainKeyMap.get("sellerid").toString();
			c.setTimeInMillis(Long.parseLong(mainKeyMap.get("bydate").toString()));
			Date byDay = c.getTime();
			Map<String, SummaryObject> submap = mainmap.getValue();
			List<ProductAdvertReportSummary> list = new LinkedList<ProductAdvertReportSummary>();
			Set<String> tempset = new HashSet<String>();
			for (Entry<String, SummaryObject> summaryitem : submap.entrySet()) {
				String subkey = summaryitem.getKey();
				Map<String, Object> subkeymap = ProductAdvertReport.getSubKeyItem(subkey);
				String sku = subkeymap.get("sku").toString();
				marketplaceid = subkeymap.get("marketplaceid").toString();
				String tempkey = sellerid+"-"+marketplaceid+"-"+sku+"-"+GeneralUtil.formatDate(byDay, "yyyy-MM-dd");
				if(tempset.contains(tempkey)){
					continue;
				} else {
					tempset.add(tempkey);
				}
				SummaryObject summaryObject = summaryitem.getValue();
				ProductAdvertReportSummary summary = new ProductAdvertReportSummary();
				summary.setBydate(byDay);
				summary.setSellerid(sellerid);
				summary.setMarketplaceid(marketplaceid);
				summary.setSku(sku);
				convertToReportSummary(summary, summaryObject);
				summary.setCtype(ctype);
				list.add(summary);
			}
			tempset.clear();
			submap.clear();
			submap = null;
			Example example = new Example(ProductAdvertReportSummary.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("sellerid", sellerid);
			crit.andEqualTo("marketplaceid", marketplaceid);
			crit.andEqualTo("bydate", byDay);
			crit.andEqualTo("ctype", ctype);
			productAdvertReportSummaryMapper.deleteByExample(example);
			productAdvertReportSummaryMapper.insertBatch(list);
		}
		summap.clear();
		summap = null;
	}

	private void convertToReportSummary(ProductAdvertReportSummary summary, SummaryObject summaryObject) {
		summary.setAcos(summaryObject.getAcos());
		summary.setRoas(summaryObject.getRoas());
		summary.setSpc(summaryObject.getSpc());
		summary.setAsin(summaryObject.getAsin());
		summary.setClicks(summaryObject.getClicks());
		summary.setCpc(summaryObject.getCpc());
		summary.setCtr(summaryObject.getCtr());
		summary.setCurrency(summaryObject.getCurrency());
		summary.setImpressions(summaryObject.getImpressions());
		summary.setOrders(summaryObject.getOrders());
		summary.setSpend(summaryObject.getSpend());
		summary.setTotalsales(summaryObject.getTotalsales());
		summary.setUnits(summaryObject.getUnits());
	}

	public void init() {
		summarymap = new ConcurrentHashMap<String, Map<String, SummaryObject>>();
	}
    public void refreshSummary(String shopid) {
    	Example example=new Example(AmzAdvAuth.class);
        Criteria crit = example.createCriteria();
		crit.andEqualTo("disable", false);
		if(StrUtil.isNotEmpty(shopid)) {
			crit.andEqualTo("shopid",shopid);
		}
		List<AmzAdvAuth> advauthlist =amzAdvAuthService.selectByExample(example);
		Calendar c =Calendar.getInstance();
        c.add(Calendar.DATE, -20);
        c.add(Calendar.HOUR, -3);
    	for(AmzAdvAuth item:advauthlist) {
    		List<AmzAdvProfile> listprofile = amzAdvAuthService.getAmzAdvProfile(item);
    		for(AmzAdvProfile profile:listprofile) {
    			Calendar begin=Calendar.getInstance();
    			begin.add(Calendar.HOUR, -1);
    			while(begin.getTime().after(c.getTime())) {
    				confirmSDByDay(profile.getSellerid(),profile.getMarketplaceid(),GeneralUtil.formatDate(begin.getTime()),profile.getId().toString());
    				confirmSPByDay(profile.getSellerid(),profile.getMarketplaceid(),GeneralUtil.formatDate(begin.getTime()),profile.getId().toString());
    				begin.add(Calendar.DATE, -1);
    			}
    			Map<String, Object> param=new HashMap<String,Object>();
        		param.put("sellerid", profile.getSellerid());
        		param.put("marketplaceid", profile.getMarketplaceid());
        		param.put("begin", GeneralUtil.formatDate(c.getTime()));
    			productAdvertReportSummaryMapper.refreshSummary(param);
    		}
        }
    }

	@Override
	public void confirmSDByDay(String sellerid, String marketplaceid, String byday, String profileid) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("sellerid", sellerid);
		param.put("marketplaceid", marketplaceid);
		param.put("byday", byday);
		param.put("profileid", profileid);
		productAdvertReportSummaryMapper.refreshSDByDay(param);
	}
	
	@Override
	public void confirmSPByDay(String sellerid, String marketplaceid, String byday, String profileid) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("sellerid", sellerid);
		param.put("marketplaceid", marketplaceid);
		param.put("byday", byday);
		param.put("profileid", profileid);
		productAdvertReportSummaryMapper.refreshSPByDay(param);
	}

	@Override
	public void addAdvReportSummary(ProductAdvertReportSummary padv) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Map<String, Object>> findAdvert(Map<String,Object> param) {
		return productAdvertReportMapper.findAdvert(param);
	}
	
}
