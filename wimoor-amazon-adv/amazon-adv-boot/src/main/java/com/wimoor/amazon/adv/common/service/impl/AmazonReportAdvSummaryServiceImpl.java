package com.wimoor.amazon.adv.common.service.impl;


import java.math.BigDecimal;
import java.math.RoundingMode;
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

	@Override
	public List<Map<String, Object>> productSaleOrder(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return productAdvertReportMapper.findProductSaleOrder(params);
	}
	
	public Map<String, Object> getProductCpcData(Map<String, Object> param) {
			    Map<String, Object> map=new HashMap<String, Object>();
			    param.put("ctype", "sd");
				List<ProductAdvertReportSummary> sdlist = productAdvertReportSummaryMapper.findSummaryData(param);
				if(sdlist!=null && sdlist.size()>0) {
					BigDecimal sdspend=new BigDecimal(0);
					int sdclick=0;
					int sdimpr=0;
					int orders=0;
					BigDecimal sales=new BigDecimal(0);
					for(int i=0;i<sdlist.size();i++) {
						ProductAdvertReportSummary sd = sdlist.get(i);
						if(sd.getTotalsales()!=null) {
							sales=sales.add(sd.getTotalsales());
						}
						if(sd.getOrders()!=null) {
							orders=orders+sd.getOrders();
						}
						if(sd.getImpressions()!=null) {
							sdimpr=sdimpr+sd.getImpressions();
						}
						if(sd.getSpend()!=null) {
							sdspend=sdspend.add(sd.getSpend());
						}
						if(sd.getClicks()!=null) {
							sdclick=sdclick+sd.getClicks();
						}
					}
					map.put("sdimpr", sdimpr);
					map.put("sdspend", sdspend);
					if(sdclick>0) {
						map.put("sd", sdspend.divide(new BigDecimal(sdclick),4,RoundingMode.HALF_UP));
						map.put("sdclick", sdclick);
						map.put("sdcr", new BigDecimal(orders).divide(new BigDecimal(sdclick),4,RoundingMode.HALF_UP));
					}else {
						map.put("sd", 0);
						map.put("sdclick", 0);
						map.put("sdcr", 0);
					}
					if(sdimpr>0) {
						map.put("sdctr", new BigDecimal(sdclick).divide(new BigDecimal(sdimpr),4,RoundingMode.HALF_UP));
					}else {
						map.put("sdctr", 0);
					}
					if(sales.compareTo(BigDecimal.ZERO)>0) {
						map.put("sdacos",  sdspend.divide(sales,4,RoundingMode.HALF_UP));
					}else {
						map.put("sdacos",  0);
					}
					
				}
				param.put("ctype", "sp");
				List<ProductAdvertReportSummary> splist = productAdvertReportSummaryMapper.findSummaryData(param);
				if(splist!=null && splist.size()>0) {
					BigDecimal sdspend=new BigDecimal(0);
					int sdclick=0;
					int sdimpr=0;
					int orders=0;
					BigDecimal sales=new BigDecimal(0);
					for(int i=0;i<splist.size();i++) {
						ProductAdvertReportSummary sd = splist.get(i);
						if(sd.getTotalsales()!=null) {
							sales=sales.add(sd.getTotalsales());
						}
						if(sd.getOrders()!=null) {
							orders=orders+sd.getOrders();
						}
						if(sd.getImpressions()!=null) {
							sdimpr=sdimpr+sd.getImpressions();
						}
						if(sd.getSpend()!=null) {
							sdspend=sdspend.add(sd.getSpend());
						}
						if(sd.getClicks()!=null) {
							sdclick=sdclick+sd.getClicks();
						}
					}
					if(sdclick>0) {
						map.put("sp", sdspend.divide(new BigDecimal(sdclick),4,RoundingMode.HALF_UP));
						map.put("spclick", sdclick);
						map.put("spimpr", sdimpr);
						map.put("spspend", sdspend);
						if(sdimpr!=0) {
							map.put("spctr", new BigDecimal(sdclick).divide(new BigDecimal(sdimpr),4,RoundingMode.HALF_UP));
						}
						if(sdclick!=0) {
							map.put("spcr", new BigDecimal(orders).divide(new BigDecimal(sdclick),4,RoundingMode.HALF_UP));
						}
						if(sales!=null&&!sales.equals(new BigDecimal(0))) {
							map.put("spacos",  sdspend.divide(sales,4,RoundingMode.HALF_UP));
						}
					}else {
						map.put("sp", 0);
						map.put("spclick", 0);
						map.put("spimpr", 0);
						map.put("spspend", 0);
						map.put("spctr", 0);
						map.put("spcr", 0);
						map.put("spacos",0);
					}
				}
			return map;
	}
	
}
