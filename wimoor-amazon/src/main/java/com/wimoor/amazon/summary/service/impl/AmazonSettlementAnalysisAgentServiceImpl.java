package com.wimoor.amazon.summary.service.impl;


 
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.mapper.MarketplaceMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
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
import com.wimoor.amazon.summary.service.IAmazonSettlementAnalysisService;
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
	public void checkSummaryData() {
		List<AmazonAuthority> amazonAuthorityList =amazonAuthorityService.getAllAuth();
		Calendar c =Calendar.getInstance();
		c.add(Calendar.DATE, -100);
		for (int step = 0; step < amazonAuthorityList.size(); step++) {
			AmazonAuthority amazonAuthority = amazonAuthorityList.get(step);
	    	 QueryWrapper<AmzSettlementAccReport> query = new QueryWrapper<AmzSettlementAccReport>();
	     	 query.eq("amazonauthid",amazonAuthority.getId());
	    	 query.gt("deposit_date",GeneralUtil.getStrDate(c.getTime()));
	    	 List<AmzSettlementAccReport> list = amzSettlementAccReportMapper.selectList(query);
	    	 for(AmzSettlementAccReport item:list) {
	    		 Double result = amzSettlementSummarySkuMapper.summaryData(item.getSettlementId());
	    		 if(result.intValue()!=0) {
	    			 item.setSumtime(null);
	    			 amzSettlementAccReportMapper.updateById(item);
	    		 }
	    	 }
	    
		}
	    	 
	}
	
	public  void refreshAll(){
	List<AmazonAuthority> amazonAuthorityList =amazonAuthorityService.getAllAuth();
	AmazonAuthority amazonAuthority;
	Calendar c =Calendar.getInstance();
	c.add(Calendar.DATE, -90);
	Calendar ctime=Calendar.getInstance();
	ctime.add(Calendar.MINUTE, -90);
	for (int step = 0; step < amazonAuthorityList.size(); step++) {
		 amazonAuthority = amazonAuthorityList.get(step);
    	 QueryWrapper<AmzSettlementAccReport> query = new QueryWrapper<AmzSettlementAccReport>();
    	 query.eq("amazonauthid",amazonAuthority.getId());
     	 query.gt("deposit_date",GeneralUtil.getStrDate(c.getTime()));
     	 query.lt("capturetime", ctime.getTime());
    	 query.isNull("sumtime");
    	 query.orderByDesc("capturetime");
			List<AmzSettlementAccReport> list = amzSettlementAccReportMapper.selectList(query);
			if(list!=null && list.size()>0) {
				for(int i=0;i<list.size()&&i<=5;i++) {
					AmzSettlementAccReport item=list.get(i);
					this.confirm(item);
					item.setSumtime(new Date());
					amzSettlementAccReportMapper.updateById(item);
				}
			}
	     }
}	 

////////////////AmzSettlementReportSummaryMonth end //////////////////////////
	public void confirm(AmzSettlementAccReport item) {
		// TODO Auto-generated method stub
	  	    Map<String,Object> param=new HashMap<String,Object>();
		    param.put("settlement_id", item.getSettlementId());
		    param.put("settlementid", item.getSettlementId());
			amzSettlementReportSummaryMonthMapper.refreshSummary(param);
			amzSettlementReportSummaryDayMapper.refreshSummary(param);
			amzSettlementSummarySkuMapper.refreshSummary(param);
			amzSettlementSummaryReturnsMapper.refreshSummary(param);
	 
		
	}
    
}