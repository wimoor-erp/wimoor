package com.wimoor.amazon.summary.service.impl;


 
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.finances.mapper.AmzSettlementAccReportMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportSummaryDayMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportSummaryMonthMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementSummaryReturnsMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementSummarySkuMapper;
import com.wimoor.amazon.finances.pojo.dto.FinDataMonthDTO;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccReport;
import com.wimoor.amazon.finances.service.IAmzSettlementSKUShareService;
import com.wimoor.amazon.finances.service.IAmzSettlementSummarySkuMonthService;
import com.wimoor.amazon.orders.mapper.AmzOrderReturnsMapper;
import com.wimoor.amazon.orders.mapper.OrdersReportMapper;
import com.wimoor.amazon.summary.service.IAmazonSettlementAnalysisService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.ProgressHelper;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
 
 
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
    IAmazonGroupService iAmazonGroupService;
	@Resource 
	private  IMarketplaceService iMarketplaceService;
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
	@Autowired
	IAmzSettlementSKUShareService iAmzSettlementSKUShareService;
	@Autowired
	IAmzSettlementSummarySkuMonthService iAmzSettlementSummarySkuMonthService;
//////////////////////////AmzSettlementReportSummaryDay end////////////////
 
	public void checkSummaryData() {
		List<AmazonAuthority> amazonAuthorityList =amazonAuthorityService.getAllAuth();
		Calendar c =Calendar.getInstance();
		c.add(Calendar.DATE, -30);
		for (int step = 0; step < amazonAuthorityList.size(); step++) {
			AmazonAuthority amazonAuthority = amazonAuthorityList.get(step);
	    	 QueryWrapper<AmzSettlementAccReport> query = new QueryWrapper<AmzSettlementAccReport>();
	     	 query.eq("amazonauthid",amazonAuthority.getId());
	    	 query.gt("deposit_date",GeneralUtil.getStrDate(c.getTime()));
	    	 List<AmzSettlementAccReport> list = amzSettlementAccReportMapper.selectList(query);
	    	 for(AmzSettlementAccReport item:list) {
	    		 if(item.getTotalAmount()==null||item.getTotalAmount().equals(new BigDecimal("0.00"))) {
	    			 continue;
	    		 }
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
    	 query.eq("invalid", false);
    	 query.orderByDesc("capturetime");
			List<AmzSettlementAccReport> list = amzSettlementAccReportMapper.selectList(query);
			if(list!=null && list.size()>0) {
				Set<String> month=new HashSet<String>();
				for(int i=0;i<list.size();i++) {
					AmzSettlementAccReport item=list.get(i);
					this.confirm(item);
					item.setSumtime(new Date());
					String monthkey=item.getMarketplaceName()+"#"+item.getSettlementStartDate().format(DateTimeFormatter.ofPattern("YYYY-MM"));
					month.add(monthkey);
					amzSettlementAccReportMapper.updateById(item);
				}
				for(String monthkey:month){
                    String[] keys=monthkey.split("#");
					String marketname=keys[0];
					String monthitme=keys[1];
					try {
						iAmzSettlementSummarySkuMonthService.summaryMonthly(amazonAuthority.getId(),marketname,GeneralUtil.getDatez(monthitme+"-01") );
					}catch(Exception e) {
						e.printStackTrace();
					}
			    }
			}
	     }
}	 
	public  void refreshAllByManual(){
		List<AmazonAuthority> amazonAuthorityList =amazonAuthorityService.getAllAuth();
		AmazonAuthority amazonAuthority;   
		for (int step = 0; step < amazonAuthorityList.size(); step++) {
			 amazonAuthority = amazonAuthorityList.get(step);
	    	 QueryWrapper<AmzSettlementAccReport> query = new QueryWrapper<AmzSettlementAccReport>();
	    	 query.eq("amazonauthid",amazonAuthority.getId());
	    	 //query.in("settlement_id", "18335746651");
	    	query.ge("deposit_date","2023-07-01");
	     	query.lt("deposit_date","2023-08-01");
	     	 List<AmzSettlementAccReport> list = amzSettlementAccReportMapper.selectList(query);
				if(list!=null && list.size()>0) {
					System.out.println("sellerid:"+amazonAuthority.getSellerid()+"     size:"+list.size()+"    time:"+new Date());
					Set<String> month=new HashSet<String>();
					for(int i=0;i<list.size();i++) {
						AmzSettlementAccReport item=list.get(i);
						iAmzSettlementSKUShareService.shareFee(item.getSettlementId());
						item.setSumtime(new Date());
						String monthkey=item.getMarketplaceName()+"#"+item.getSettlementStartDate().format(DateTimeFormatter.ofPattern("YYYY-MM"));
						month.add(monthkey);
						System.out.println(monthkey+"    time:"+new Date());
						amzSettlementAccReportMapper.updateById(item);
					}
					for(String monthkey:month){
                        String[] keys=monthkey.split("#");
						String marketname=keys[0];
						String monthitme=keys[1];
						System.out.println("marketname:"+marketname+"   monthitme:"+monthitme+"    time:"+new Date());
						try {
							iAmzSettlementSummarySkuMonthService.summaryMonthly(amazonAuthority.getId(),marketname,GeneralUtil.getDatez(monthitme+"-01") );
						}catch(Exception e) {
							e.printStackTrace();
						}
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
			try {
				iAmzSettlementSKUShareService.shareFee(item.getSettlementId());
			}catch(Exception e) {
				e.printStackTrace();
			}
			
	}
 
	@Override
	public void refreshData(ProgressHelper progressHelper, FinDataMonthDTO dto) {
		// TODO Auto-generated method stub
		String groupid=dto.getGroupid();
		String marketplaceid=dto.getMarketplaceid();
		String startdate=dto.getFromDate();
		String enddate=dto.getEndDate();
		String datetype=dto.getFtype();
		String isdeep=dto.getIsdeep();
		 int i=0;
		 progressHelper.setProgress(1);
		 String marketname=null;
		 List<AmazonAuthority> listauth=new LinkedList<AmazonAuthority>();
		 if(StrUtil.isNotBlank(groupid)&&StrUtil.isNotBlank(marketplaceid)) {
		     Marketplace market = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
		     marketname=market.getPointName();
		     AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
		     listauth.add(auth);
		 }else if(StrUtil.isNotBlank(groupid)) {
			 listauth=amazonAuthorityService.selectByGroupId(groupid);
		 }else if(StrUtil.isNotBlank(marketplaceid)) {
			 List<AmazonGroup> groups = iAmazonGroupService.selectByShopId(dto.getShopid());
			 Marketplace market = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
			 marketname=market.getPointName();
			 for(AmazonGroup group:groups) {
				  AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(group.getId(), marketplaceid);
				  listauth.add(auth);
			 }
		 }else {
			 listauth=amazonAuthorityService.selectByshopid(dto.getShopid());
		 }
		 
	     for(AmazonAuthority auth:listauth) {
	         i++;
	    	 List<String>  marketnames=new LinkedList<String>();
	    	 if(marketname!=null) {
	    		 marketnames.add(marketname);
	    	 }else {
	    		List<Marketplace> markets = iMarketplaceService.selectMarketBySellerid(auth.getSellerid(), auth.getShopId());
	    		for(Marketplace market:markets) {
	    			marketnames.add(market.getPointName());
	    		}
	    	 }
	    	 int j=0;
	    	 for(String mktname:marketnames) {
	    	     j++;
	    	     double rate = (i*1.0)/listauth.size()*75.00+(j*1.0)/marketnames.size()*5.00;
	    		 Set<String> month=new HashSet<String>();
			     SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM");
		        if(isdeep!=null&&isdeep.equals("true")) {
		       	    Map<String,Object> param=new HashMap<String,Object>();
		            param.put("groupid", auth.getGroupid());
		            param.put("shopid", auth.getShopId());
		            param.put("marketplace_name", mktname);
		            param.put("fromDate", startdate);
		            param.put("endDate", enddate);
		            if(datetype!=null&&datetype.equals("acc")) {
		           	    param.put("datetype", datetype);
		            }else {
		           	 param.put("datetype", null);
		            }
		       	 List<AmzSettlementAccReport> list = amzSettlementAccReportMapper.findlist(param);
		         int k=0;
		       	 for(AmzSettlementAccReport item:list) {
		       		 try {
							iAmzSettlementSKUShareService.shareFee(item.getSettlementId());
						}catch(Exception e) {
							e.printStackTrace();
						}
		       		 k++;
		       		 Double progress  =rate+(k/list.size())*9.00;
		       		 progressHelper.setProgress(progress.intValue());
		       		 month.add(item.getSettlementStartDate().format( DateTimeFormatter.ofPattern("YYYY-MM")));
		       		 month.add(item.getSettlementEndDate().format( DateTimeFormatter.ofPattern("YYYY-MM")));
		       	 }
		       	
		        }else {
		       	 Date start = GeneralUtil.getDatez(startdate);
		       	 Date end = GeneralUtil.getDatez(enddate);
		       	 Calendar c=Calendar.getInstance();
		       	 c.setTime(start);
		       	 month.add(fmt.format(start));
		   		 month.add(fmt.format(end));
		       	 while(c.getTime().before(end)) {
		       		 month.add(fmt.format(c.getTime()));
		       		 c.add(Calendar.MONTH, 1);
		       	 }
		        }
		        int k=0;
				for(String monthitme:month){
					     k++;
 					     Double progress =rate+(k/month.size())*9.00+10.00;
			       		 progressHelper.setProgress(progress.intValue());
		            	 iAmzSettlementSummarySkuMonthService.summaryMonthly(auth.getId(),mktname,GeneralUtil.getDatez(monthitme+"-01"));
				}
	    	 }
	     } 
		progressHelper.setProgress(100);
	}
    
}