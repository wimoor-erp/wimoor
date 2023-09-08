package com.wimoor.amazon.adv.controller;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.adv.common.service.IAmazonReportAdvSummaryService;
import com.wimoor.amazon.adv.common.service.IAmzAdvPortfoliosService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportHandlerService;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportService;
import com.wimoor.amazon.adv.common.service.IAmzAdvSnapshotHandlerService;
import com.wimoor.amazon.adv.common.service.impl.AmzAdvReportHandlerServiceImpl.AdvRecordType;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;


@Component("schedulingConfigController")
@RestController 
@RequestMapping("/api/v1/advschedule") 
public class SchedulingConfigController {
	@Resource
	IAmzAdvPortfoliosService amzAdvPortfoliosService;
	@Resource
	IAmzAdvReportService amzAdvReportService;
	@Resource
	IAmzAdvReportHandlerService amzAdvReportHandlerService;
	@Resource
	IAmzAdvRemindService amzAdvRemindService;
    @Resource
    IAmazonReportAdvSummaryService amazonReportAdvSummaryService;
    @Resource
    IAmzAdvSnapshotHandlerService iAmzAdvSnapshotHandlerService;
	@GetMapping("/refreshListPortfolios")
	public Result<?>  refreshListPortfolios() {
	     // TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 amzAdvPortfoliosService.amzGetListPortfoliosForCampaign();
			}
		}).start();
		 return Result.success();
	}

	@GetMapping("/requestReport")
	public Result<?> requestReport(){
		 Calendar c=Calendar.getInstance();
		 int[] days= {4,10,33};
		 int day=days[c.get(Calendar.HOUR_OF_DAY)%3];
		 c.add(Calendar.DATE, day*-1);
		 amzAdvReportHandlerService.requestReport(c.getTime(),null,false);
		 
		 int[] days2= {2,3,4,5,6};
		 int day2=days2[c.get(Calendar.HOUR_OF_DAY)%5];
		 c.add(Calendar.DATE, day2*-1);
		 amzAdvReportHandlerService.requestReport(c.getTime(),null,true);
		 return Result.success();
	}
	
 

	@GetMapping("/requestReportByDay")
	public Result<?> requestReportByDay(String shopid,String start,String end){
	     Date startDate = GeneralUtil.getDatez(start);
	     Date endDate = GeneralUtil.getDatez(end);
	     Calendar c=Calendar.getInstance();
	     c.setTime(startDate);
	     while(c.getTime().before(endDate)) {
	    	 amzAdvReportHandlerService.requestReport(c.getTime(),shopid,true);
	    	 c.add(Calendar.DATE, 1);
	     }
		return Result.success();
	}
	
	@GetMapping("/refreshRemaind")
	public Result<?> refreshRemaindAction() {
				// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 amzAdvRemindService.refreshRemaind();
			}
		}).start();
				
		return Result.success();
	}
	
	@GetMapping("/refreshSummaryByShopid")
	public Result<?> refreshSummaryAction(String shopid) {
				// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amazonReportAdvSummaryService.refreshSummary(shopid);
			}
		}).start();
				
		return Result.success();
	}


	@GetMapping("/requestSnapshot")
	public Result<?> requestSnapshot(){
		     iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.campaigns, CampaignType.sp);
		     iAmzAdvSnapshotHandlerService.readSnapshot();
		     iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.campaigns, CampaignType.hsa);
		     iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.adGroups, CampaignType.sp);
		     iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.keywords, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.keywords, CampaignType.hsa);
			 iAmzAdvSnapshotHandlerService.readSnapshot();
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.productAds, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.negativeKeywords, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.campaignNegativeKeywords, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.readSnapshot();
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.targets, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.negativeTargets, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.campaigns, CampaignType.sd);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.adGroups, CampaignType.sd);
			 iAmzAdvSnapshotHandlerService.readSnapshot();
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.productAds, CampaignType.sd);
			 iAmzAdvSnapshotHandlerService.readSnapshot();
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.targets,CampaignType.sd);
			 iAmzAdvSnapshotHandlerService.requestHsaVideoSnapshot();
		 return Result.success();
	}
	
	@GetMapping("/readReport")
	public Result<?> readReport(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amzAdvReportHandlerService.readReport(null);
			}
		}).start();
		 return Result.success();
	}
	
	@GetMapping("/readReportByShopid")
	public Result<?> readReport(String shopid){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amzAdvReportHandlerService.readReport(shopid);
			}
		}).start();
		 return Result.success();
	}
	
	@GetMapping("/readSnapshot")
	public Result<?> readSnapshot(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				iAmzAdvSnapshotHandlerService.readSnapshot();
			}
		}).start();
		 return Result.success();
	}
	
	@GetMapping("/readSnapshotById")
	public Result<?> readSnapshotById(String id){
		iAmzAdvSnapshotHandlerService.readSnapshot(id);
		 return Result.success();
	}
	@GetMapping("/requestHsaVideoSnapshot")
	public Result<?> requestHsaVideoSnapshot(String id){
		iAmzAdvSnapshotHandlerService.requestHsaVideoSnapshot();
		 return Result.success();
	}
}
