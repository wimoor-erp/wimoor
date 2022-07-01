package com.wimoor.amazon.report.summary.service.impl;


 
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.mapper.MarketplaceMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.report.mapper.AmzOrderReturnsMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementAccReportMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementReportMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementReportSummaryDayMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementReportSummaryMonthMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementSummaryReturnsMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementSummarySkuMapper;
import com.wimoor.amazon.report.mapper.OrdersReportMapper;
import com.wimoor.amazon.report.pojo.entity.AmzSettlementAccReport;
import com.wimoor.amazon.report.summary.service.IAmazonSettlementAnalysisService;
import com.wimoor.common.GeneralUtil;
 
 
@Service("amazonSettlementAnalysisAgentService")  
public   class AmazonSettlementAnalysisAgentServiceImpl implements IAmazonSettlementAnalysisService {
	HashMap<String,Map<String,Integer>> settlement_order=null;
	@Resource
	private AmzSettlementReportMapper amzSettlementReportMapper;
	@Resource
	AmzSettlementReportSummaryDayMapper amzSettlementReportSummaryDayMapper;
	@Resource
    IAmazonAuthorityService amazonAuthorityService;
	@Resource 
	private  MarketplaceMapper marketplaceMapper;
	@Resource
	AmzSettlementSummarySkuMapper amzSettlementSummarySkuMapper;
	@Resource
	AmzSettlementReportSummaryMonthMapper amzSettlementReportSummaryMonthMapper;
	@Resource
	OrdersReportMapper ordersReportMapper;
	@Resource
	AmzOrderReturnsMapper amzOrderReturnsMapper;
	@Resource
	AmzSettlementSummaryReturnsMapper amzSettlementSummaryReturnsMapper;
	@Resource
	private AmzSettlementAccReportMapper amzSettlementAccReportMapper;
//////////////////////////AmzSettlementReportSummaryDay end////////////////
	 public List<AmazonAuthority> getAvailableAmazonAuthority() {
		QueryWrapper<AmazonAuthority> query = new QueryWrapper<AmazonAuthority>();
		query.eq("disable", false);
		return amazonAuthorityService.list(query);
	}
	
	public  void refreshAll(){
	List<AmazonAuthority> amazonAuthorityList = getAvailableAmazonAuthority();
	AmazonAuthority amazonAuthority;
	Calendar c =Calendar.getInstance();
	c.add(Calendar.DATE, -60);
	for (int step = 0; step < amazonAuthorityList.size(); step++) {
		 amazonAuthority = amazonAuthorityList.get(step);
			List<Marketplace> marketplaceList = marketplaceMapper.findbyauth(amazonAuthority.getId());
			for(Marketplace marketplace:marketplaceList) {
				     amazonAuthority.setMarketPlace(marketplace);
				      Set<String> set =new HashSet<String>();
				    	 set.add(marketplace.getPointName());
				    	 Map<String,Object> param=new HashMap<String,Object>();
				    	 QueryWrapper<AmzSettlementAccReport> query = new QueryWrapper<AmzSettlementAccReport>();
				    	 query.eq("amazonauthid",amazonAuthority.getId());
				    	 query.eq("marketplaceName",marketplace.getPointName());
				    	 query.gt("depositDate",GeneralUtil.getStrDate(c.getTime()));
				    	 query.lt("depositDate",GeneralUtil.getStrDate(new Date()));
							List<AmzSettlementAccReport> list = amzSettlementAccReportMapper.selectList(query);
							if(list!=null && list.size()>0) {
								for(AmzSettlementAccReport item:list) {
									param.put("beginDate",item.getSettlementStartDate());
									param.put("endDate",item.getSettlementEndDate());
									param.put("amazonAuthId", amazonAuthority.getId());
									param.put("marketplace_name",marketplace.getPointName());
									param.put("settlement_id", item.getSettlementId());
									param.put("settlementid", item.getSettlementId());
									this.confirm(param, set);
								}
							}
			}
	}
	
}	 

////////////////AmzSettlementReportSummaryMonth end //////////////////////////
	public void confirm(Map<String, Object> param, Set<String> marketnameset) {
		// TODO Auto-generated method stub
		for(String marketname:marketnameset) {
			param.put("marketplace_name", marketname);
			amzSettlementReportSummaryMonthMapper.refreshSummary(param);
			amzSettlementReportSummaryDayMapper.refreshSummary(param);
			amzSettlementSummarySkuMapper.refreshSummary(param);
			amzSettlementSummaryReturnsMapper.refreshSummary(param);
		}
		
	}
    
}