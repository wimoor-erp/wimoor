package com.wimoor.amazon.adv.common.service.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.dao.ProductAdvertReportMapper;
import com.wimoor.amazon.adv.common.dao.ProductAdvertReportSummaryMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
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
     
    public void refreshSummary(String shopid) {
    	Example example=new Example(AmzAdvAuth.class);
        Criteria crit = example.createCriteria();
		crit.andEqualTo("disable", false);
		List<AmzAdvAuth> advauthlist =null;
		if(StrUtil.isNotBlank(shopid)) {
			crit.andEqualTo("shopid",shopid);
		} 
		advauthlist=amzAdvAuthService.selectByExample(example);
		Calendar c =Calendar.getInstance();
        c.add(Calendar.DATE, -20);
        c.add(Calendar.HOUR, -3);
    	for(AmzAdvAuth item:advauthlist) {
    		List<AmzAdvProfile> listprofile = amzAdvAuthService.getAmzAdvProfile(item);
    		for(AmzAdvProfile profile:listprofile) {
				confirmSDByDay(profile.getSellerid(),profile.getMarketplaceid(),c.getTime(),new Date(),profile.getId().toString());
				confirmSPByDay(profile.getSellerid(),profile.getMarketplaceid(),c.getTime(),new Date(),profile.getId().toString());
    			Map<String, Object> param=new HashMap<String,Object>();
        		param.put("sellerid", profile.getSellerid());
        		param.put("marketplaceid", profile.getMarketplaceid());
        		param.put("startdate", GeneralUtil.formatDate(c.getTime()));
        		param.put("begin", GeneralUtil.formatDate(c.getTime()));
        		param.put("enddate", GeneralUtil.formatDate(new Date()));
        		param.put("end", GeneralUtil.formatDate(new Date()));
        		param.put("profileid", profile.getId());
    			productAdvertReportSummaryMapper.refreshSummary(param);
    		}
        }
    }

	@Override
	public void confirmSDByDay(String sellerid, String marketplaceid, Date startdate,Date enddate, String profileid) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("sellerid", sellerid);
		param.put("marketplaceid", marketplaceid);
		param.put("startdate", startdate);
		param.put("enddate", enddate);
		param.put("profileid", profileid);
		productAdvertReportSummaryMapper.refreshSDByDay(param);
	}
	
	@Override
	public void confirmSPByDay(String sellerid, String marketplaceid,Date startdate,Date enddate,String profileid) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("sellerid", sellerid);
		param.put("marketplaceid", marketplaceid);
		param.put("startdate", startdate);
		param.put("enddate", enddate);
		param.put("profileid", profileid);
		productAdvertReportSummaryMapper.refreshSPByDay(param);
	}

	 
	public List<Map<String, Object>> findAdvert(Map<String,Object> param) {
		return productAdvertReportMapper.findAdvert(param);
	}
	
}
